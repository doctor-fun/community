package com.newcoder.community.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


//
//创建邮件主体，发送出去
@Component
public class MailClient {
    private static final Logger logger= LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to ,String subject,String content) throws MessagingException {
        //MimeMessage是邮件主体，模板对象mimemessage,空的
        MimeMessage message=javaMailSender.createMimeMessage();
        //构建message里面的内容
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true);//true打开Html文本支持
        javaMailSender.send(helper.getMimeMessage());
    }
}
