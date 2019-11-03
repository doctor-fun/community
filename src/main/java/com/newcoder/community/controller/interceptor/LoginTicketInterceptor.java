package com.newcoder.community.controller.interceptor;


import com.newcoder.community.model.LoginTicket;
import com.newcoder.community.model.User;
import com.newcoder.community.service.UserService;
import com.newcoder.community.utils.CookieUtil;
import com.newcoder.community.utils.HostHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    //ctrl+i快捷键
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;//为本线程生成threadlocal变量,用于保存登录用户的ticket
    @Override//获得了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      //注册以后会有ticket,在任何请求来之前看一下cookie里面有没有ticket，有的话就要做一些操作，页面上回根据
       String ticket = CookieUtil.getValue(request,"ticket");
        if(ticket!=null){
            LoginTicket loginTicket=userService.findLoginTicket(ticket);
            //检查凭证是否有效
            if(loginTicket!=null&&loginTicket.getStatus()==0&&loginTicket.getExpired().after(new Date())){
                User  user= userService.findUserById(loginTicket.getUserId());
                //在本次请求中持有用户
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user=hostHolder.getUser();
        if(user!=null && modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }
    }

    @Override// 清理用户信息
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
