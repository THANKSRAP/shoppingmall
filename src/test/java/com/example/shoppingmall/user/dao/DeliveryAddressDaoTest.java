package com.example.shoppingmall.user.dao;

import com.example.shoppingmall.user.domain.DeliveryAddressDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class DeliveryAddressDaoTest {

    @Autowired
    private DeliveryAddressDao deliveryAddressDao;

    private DeliveryAddressDto baseDto;

    @Before
    public void setUp() {
        deliveryAddressDao.deleteAllByUserId(1L);

        baseDto = new DeliveryAddressDto();
        baseDto.setUserId(1L);
        baseDto.setRecipientName("홍길동");
        baseDto.setRecipientPhoneNumber("010-1234-5678");
        baseDto.setRecipientEmail("gil@example.com");
        baseDto.setPostalCode("12345");
        baseDto.setMainAddress("서울특별시 강남구");
        baseDto.setDetailedAddress("101동 1001호");
        baseDto.setDefault(true);
    }

    @Test
    public void shouldInsertAndRetrieveDeliveryAddress() {
        deliveryAddressDao.insertDeliveryAddress(baseDto);
        List<DeliveryAddressDto> list = deliveryAddressDao.selectActiveAddressesByUserId(1L);
        assertEquals(1, list.size());
        assertEquals("홍길동", list.get(0).getRecipientName());
        assertTrue(list.get(0).isDefault());
    }

    @Test(expected = Exception.class)
    public void shouldFailToInsertWhenRequiredFieldsMissing() {
        DeliveryAddressDto invalidDto = new DeliveryAddressDto();
        invalidDto.setUserId(1L);
        invalidDto.setMainAddress("서울시");
        deliveryAddressDao.insertDeliveryAddress(invalidDto);
    }

    @Test
    public void shouldResetAndUpdateDefaultAddress() {
        deliveryAddressDao.insertDeliveryAddress(baseDto);
        deliveryAddressDao.resetDefaultAddressByUserId(1L);
        List<DeliveryAddressDto> list = deliveryAddressDao.selectActiveAddressesByUserId(1L);
        assertFalse(list.get(0).isDefault());

        deliveryAddressDao.updateIsDefaultById(list.get(0).getDeliveryAddressId(), true);
        DeliveryAddressDto updated = deliveryAddressDao.selectActiveAddressesByUserId(1L).get(0);
        assertTrue(updated.isDefault());
    }

    @Test
    public void shouldDeactivateAddressAndNotRetrieveIt() {
        deliveryAddressDao.insertDeliveryAddress(baseDto);
        Long id = deliveryAddressDao.selectActiveAddressesByUserId(1L).get(0).getDeliveryAddressId();
        deliveryAddressDao.deactivateAddressById(id);
        List<DeliveryAddressDto> list = deliveryAddressDao.selectActiveAddressesByUserId(1L);
        assertTrue(list.isEmpty());
    }

    @Test
    public void shouldSortDeliveryAddressesCorrectly() throws InterruptedException {
        baseDto.setDefault(false);
        deliveryAddressDao.insertDeliveryAddress(baseDto);
        Thread.sleep(10);

        DeliveryAddressDto secondDto = new DeliveryAddressDto();
        secondDto.setUserId(1L);
        secondDto.setRecipientName("고길동");
        secondDto.setRecipientPhoneNumber("010-5678-1234");
        secondDto.setRecipientEmail("ko@example.com");
        secondDto.setPostalCode("12345");
        secondDto.setMainAddress("서울특별시 강남구");
        secondDto.setDetailedAddress("102동 1002호");
        secondDto.setDefault(true);

        deliveryAddressDao.insertDeliveryAddress(secondDto);
        List<DeliveryAddressDto> list = deliveryAddressDao.selectActiveAddressesByUserId(1L);

        assertEquals(2, list.size());
        assertEquals("고길동", list.get(0).getRecipientName()); // 기본 배송지가 먼저 나와야 함
    }

    @Test
    public void shouldFindOrReturnNullForSameAddress() {
        deliveryAddressDao.insertDeliveryAddress(baseDto);
        DeliveryAddressDto matched = deliveryAddressDao.findActiveAddressByUserIdAndAddress(baseDto);
        assertNotNull(matched);

        baseDto.setMainAddress("다른 주소");
        DeliveryAddressDto notMatched = deliveryAddressDao.findActiveAddressByUserIdAndAddress(baseDto);
        assertNull(notMatched);
    }
}