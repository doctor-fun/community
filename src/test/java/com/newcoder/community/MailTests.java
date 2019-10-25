package com.newcoder.community;

import com.newcoder.community.utils.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {


    @Autowired
    private MailClient mailClient;

    //主动调用模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTestMail(){
        try {
            mailClient.sendMail("lxcc432@163.com","我发的","来了来了");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    @Test

    //通过模板引擎生成动态网页
    public void testHtmlMail(){
        Context context=new Context();
        context.setVariable("username","sunday");//参数名，参数值

        String content = templateEngine.process("/mail/demo",context);//渲染生成网页结果
        System.out.println(content);
        try {
            mailClient.sendMail("lxcc432@163.com","html",content);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
