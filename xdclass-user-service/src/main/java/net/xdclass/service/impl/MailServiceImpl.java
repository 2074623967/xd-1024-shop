package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 邮箱服务
 *
 * @author tangcj
 * @date 2024/09/09 10:38
 **/
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    /**
     * Spring Boot 提供了一个发送邮件的简单抽象，直接注入即可使用
     */
    @Resource
    private JavaMailSender javaMailSender;

    /**
     * 配置文件中的发送邮箱
     */
    @Value("${spring.mail.from}")
    private String from;

    /**
     * 发送邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendMail(String to, String subject, String content) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(from);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        javaMailSender.send(message);
        log.info("邮件发成功:{}", message.toString());
    }

}
