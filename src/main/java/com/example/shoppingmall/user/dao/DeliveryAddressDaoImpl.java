package com.example.shoppingmall.user.dao;

import com.example.shoppingmall.user.domain.DeliveryAddressDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeliveryAddressDaoImpl implements DeliveryAddressDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String NAMESPACE = "DeliveryAddressMapper.";

    @Override
    public List<DeliveryAddressDto> selectActiveAddressesByUserId(Long userId) {
        return sqlSession.selectList(NAMESPACE + "selectActiveAddressesByUserId", userId);
    }

    @Override
    public DeliveryAddressDto findActiveAddressByUserIdAndAddress(DeliveryAddressDto dto) {
        return sqlSession.selectOne(NAMESPACE + "findActiveAddressByUserIdAndAddress", dto);
    }

    @Override
    public DeliveryAddressDto findById(Long deliveryAddressId) {
        return sqlSession.selectOne(NAMESPACE + "findById", deliveryAddressId);
    }

    @Override
    public int updateIsDefaultById(@Param("deliveryAddressId") Long deliveryAddressId,
                            @Param("isDefault") boolean isDefault) {
        return sqlSession.update(NAMESPACE + "updateIsDefaultById",
                new java.util.HashMap<String, Object>() {{
                    put("deliveryAddressId", deliveryAddressId);
                    put("isDefault", isDefault);
                }});
    }

    @Override
    public int resetDefaultAddressByUserId(Long userId) {
        return sqlSession.update(NAMESPACE + "resetDefaultAddressByUserId", userId);
    }

    @Override
    public int deactivateAddressById(Long deliveryAddressId) {
        return sqlSession.update(NAMESPACE + "deactivateAddressById", deliveryAddressId);
    }

    @Override
    public int insertDeliveryAddress(DeliveryAddressDto dto) {
        return sqlSession.insert(NAMESPACE + "insertDeliveryAddress", dto);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        sqlSession.delete(NAMESPACE + "deleteAllByUserId", userId);
    }
}
