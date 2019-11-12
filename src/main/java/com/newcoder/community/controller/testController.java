package com.newcoder.community.controller;

import com.newcoder.community.dao.DiscussPostMapper;
import com.newcoder.community.dao.UserMapper;
import com.newcoder.community.model.DiscussPost;
import com.newcoder.community.model.User;
import com.newcoder.community.utils.CommunityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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
