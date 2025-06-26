package com.example.shoppingmall.user.service;

import com.example.shoppingmall.user.dao.DeliveryAddressDao;
import com.example.shoppingmall.user.domain.DeliveryAddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DeliveryAddressService {
    @Autowired
    private DeliveryAddressDao deliveryAddressDao;

    public List<DeliveryAddressDto> getActiveAddresses(Long userId) {
        return deliveryAddressDao.selectActiveAddressesByUserId(userId);
    }

    @Transactional
    public void upsertAddress(DeliveryAddressDto newAddressDto) {
        Long userId = newAddressDto.getUserId();

        // 동일 주소로 등록된 is_active=true 배송지 찾기
        DeliveryAddressDto existing = deliveryAddressDao.findActiveAddressByUserIdAndAddress(newAddressDto);

        if (existing != null) {
            boolean recipientEqual =
                    Objects.equals(existing.getRecipientName(), newAddressDto.getRecipientName()) &&
                            Objects.equals(existing.getRecipientPhoneNumber(), newAddressDto.getRecipientPhoneNumber()) &&
                            Objects.equals(existing.getRecipientEmail(), newAddressDto.getRecipientEmail());

            if (recipientEqual) {
                // 수신자 정보 같고, is_default만 다르면 → is_default만 수정
                if (!Objects.equals(existing.isDefault(), newAddressDto.isDefault())) {
                    // is_default가 true라면
                    if (Boolean.TRUE.equals(newAddressDto.isDefault())) {
                        // 기존 기본 배송지 해제
                        deliveryAddressDao.resetDefaultAddressByUserId(userId);
                    }
                    deliveryAddressDao.updateIsDefaultById(existing.getDeliveryAddressId(), newAddressDto.isDefault());
                }
                return;
            }

            // 기존 주소 정보와 다름 → 기존 비활성화
            deliveryAddressDao.deactivateAddressById(existing.getDeliveryAddressId());
        }

        // 새 주소 insert 전, 기본배송지인 경우 기존 기본배송지 해제
        if (Boolean.TRUE.equals(newAddressDto.isDefault())) {
            deliveryAddressDao.resetDefaultAddressByUserId(userId);
        }

        deliveryAddressDao.insertDeliveryAddress(newAddressDto);
    }
}
