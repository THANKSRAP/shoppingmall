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
}
