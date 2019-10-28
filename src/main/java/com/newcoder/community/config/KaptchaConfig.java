package com.newcoder.community.config;


import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
//验证码图片的配置->使用kaptcha的基础功能->整合一下
public class KaptchaConfig {
    @Bean
    public Producer kaptchaProducer(){
//        kaptcha提供生成文字和根据文字生成文件的功能

        Properties  properties=new Properties();
        properties.setProperty("kaptcha.image.width","100");//单位是像素
        properties.setProperty("kaptcha.image.height","40");
        properties.setProperty("kaptcha.textproducer.font.size","32");
        properties.setProperty("kaptcha.textproducer.font.color","black");//3原色
        properties.setProperty("kaptcha.textproducer.char.string","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        properties.setProperty("kaptcha.textproducer.char.length","4");//从上面字符中随机生成4个
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");//图片污染级别

        DefaultKaptcha kaptcha=new DefaultKaptcha();
        Config config=new Config(properties);//kaptcha提供的配置包装类，包装一下
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
