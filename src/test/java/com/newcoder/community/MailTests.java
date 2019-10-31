package com.newcoder.community;

import com.newcoder.community.dao.LoginTicketMapper;
import com.newcoder.community.model.LoginTicket;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {


    @Autowired
    private MailClient mailClient;

    //主动调用模板引擎
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
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
    @Test
    public void testLogin(){
//
//        LoginTicket loginTicket=new LoginTicket();
//        loginTicket.setUserId(5);
//        loginTicket.setTicket("babazaici");
//        loginTicket.setStatus(0);
//
////        try {
////            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
////            Date date1=dateFormat.parse("2019-12-31");
////
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
//
//        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
//        loginTicketMapper.insertLoginTicket(loginTicket);
        LoginTicket loginTicket=loginTicketMapper.selectByTicket("babazaici");
        System.out.println(loginTicket);


    }
}
