package net.xdclass;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 邮件发送测试
 *
 * @author tangcj
 * @date 2024/09/09 10:52
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class MailTest {

    @Resource
    private MailService mailService;


    @Test
    public void testSendMail() {

        mailService.sendMail("2074623967@qq.com", "欢迎来到小滴课堂，学习主流it技术", "哈哈，这个就是内容，https://xdclass.net");
    }

}

