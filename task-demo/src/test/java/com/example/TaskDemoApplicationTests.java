package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskDemoApplicationTests {

    @Resource
    private JavaMailSenderImpl mailSender;

    /**
     * 发送一个简单邮件
     */
    @Test
    public void contextLoads() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("laser@oristartech.com");
        message.setSubject("通知");
        message.setText("今天周四");
        message.setTo("linshuai0709@126.com"); // , "shuailinsuccess@163.com"
        mailSender.send(message);
    }

    /**
     * 发送一个带附件的邮件
     * @throws MessagingException
     */
    @Test
    public void test() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("893577263@qq.com");
        helper.setSubject("通知");
        helper.setText("<b style='color:red;'>今天周二</b>", true);
        helper.setTo("song_qingwei@sina.com");
        helper.addAttachment("1.jpg", new File("E:\\数码辰星\\基于bootstrap物资管理系统后台模板源码\\img\\logo.jpg"));
        helper.addAttachment("2.jpg", new File("E:\\数码辰星\\基于bootstrap物资管理系统后台模板源码\\img\\logo.jpg"));
        mailSender.send(mimeMessage);
    }

}
