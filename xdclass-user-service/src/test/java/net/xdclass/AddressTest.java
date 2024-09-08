package net.xdclass;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.AddressDO;
import net.xdclass.service.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 用户服务测试
 *
 * @author tangcj
 * @date 2024/09/08 10:23
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class AddressTest {

    @Autowired
    private AddressService addressService;

    @Test
    public void testAddressDetail(){

        AddressDO addressDO = addressService.getDetail(1L);
        log.info(addressDO.toString());
    }


}