package com.newcoder.community.controller;

import com.newcoder.community.model.DiscussPost;
import com.newcoder.community.model.Page;
import com.newcoder.community.model.User;
import com.newcoder.community.service.DiscussPostService;
import com.newcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model,Page page ){
        // 方法调用钱,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.所以controller不用传page的这一步
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
//discussPost只保存了每个评论所属用户id，还需要为每条评论增加用户名，所以需要一个新map
       List<DiscussPost> list= discussPostService.findDiscussPosts(0,page.getOffset(),page.getLimit());
        List<Map<String,Object>> discussPosts=new ArrayList<>();
       if(list!=null)
       {
           for(DiscussPost post:list){
               Map<String,Object> map=new HashMap<>();
               map.put("post",post);
               User user=userService.findUserById(post.getUserId());
               map.put("user",user);
               discussPosts.add(map);
           }

       }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
}
