package com.newcoder.community.utils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
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

    /**
     *
     * @param code 代码
     * @param msg 提示信息
     * @param map //业务数据
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json=new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for(String key:map.keySet()){
                json.put(key,map.get(key));//往json里放map键值对
            }
        }
        return json.toJSONString();

    }
    /**
     *重载
     */
    public static String getJSONString(int code,String msg){
        return getJSONString(code,msg,null);
    }
    public static String getJSONString(int code){
        return getJSONString(code,null,null );
    }
//
//    /**
//     * 测试类
//     */
//{"msg":"ok","code":0,"name":"张三","age":25}
//    public static void main(String[] args){
//        Map<String , Object> map=new HashMap<>();
//        map.put("name","张三");
//        map.put("age",25);
//        System.out.println(getJSONString(0,"ok",map));
//    }
    //



}
