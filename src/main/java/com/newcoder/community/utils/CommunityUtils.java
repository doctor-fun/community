package com.newcoder.community.utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtils {
    //生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");//将-替换为无
        //md5加密
        //密码+salt

    }
    //加密成16进制的结果返回，带盐
    public static String md5(String key){

        if(StringUtils.isBlank(key)){
            return null;
        }

        return DigestUtils.md5DigestAsHex(key.getBytes());

    }


}
