package com.newcoder.community.controller;

import com.newcoder.community.utils.CommunityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/alpha")
public class testController {
    //用来测试返回json数据，前端静态页面/static/html/ajaxDemo.html会对这个点进行请求
    @RequestMapping(path="ajax",method= RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtils.getJSONString(0,"操作成功");

    }
}
