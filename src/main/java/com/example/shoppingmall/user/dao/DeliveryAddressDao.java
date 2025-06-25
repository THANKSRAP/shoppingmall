package com.example.shoppingmall.user.dao;

import com.example.shoppingmall.user.domain.DeliveryAddressDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeliveryAddressDao {
    List<DeliveryAddressDto> selectActiveAddressesByUserId(Long userId);

    DeliveryAddressDto findActiveAddressByUserIdAndAddress(DeliveryAddressDto dto);

    DeliveryAddressDto findById(Long deliveryAddressId);

    int updateIsDefaultById(@Param("deliveryAddressId") Long deliveryAddressId,
                            @Param("isDefault") boolean isDefault);

    int resetDefaultAddressByUserId(Long userId);

    int deactivateAddressById(Long deliveryAddressId);

    int insertDeliveryAddress(DeliveryAddressDto dto);

    void deleteAllByUserId(Long userId);
}
