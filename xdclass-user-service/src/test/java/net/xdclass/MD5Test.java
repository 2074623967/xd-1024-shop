package net.xdclass;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.utils.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试MD5
 *
 * @author tangcj
 * @date 2024/09/12 18:26
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class MD5Test {

    @Test
    public void testMd5() {
        log.info(CommonUtil.MD5("123456"));
    }
}

