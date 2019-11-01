package com.newcoder.community.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
//获取固定名称的cookie值
public class CookieUtil {
    public static String getValue(HttpServletRequest request,String name){
        if(request==null|| name==null){
            throw new IllegalArgumentException("参数为空!");
        }
        Cookie[] cookies=request.getCookies();//获得所有cookie;
        if(cookies!=null){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }

        }
        return null;
    }
}
