package com.newcoder.community.controller.interceptor;


import com.newcoder.community.annotation.LoginRequired;
import com.newcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
//先拦截住请求，然后将请求的方法体找到，获得它的注解
//检查threadlocal是否有user，没有说明没登录
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    //handler是被拦截的方法，但是不能取得方法， 将它强转为可以拿到方法的类handlerMethod
//    以访问的jsp为：http://localhost:8080/dmsd-itoo-exam-log-web/course/index.jsp，工程名为/dmsd-itoo-exam-log-web为例：
//
//            request.getContextPath()，得到工程名：/dmsd-itoo-exam-log-web；
//
//            request.getServletPath()，返回当前页面所在目录下全名称：/course/index.jsp；
//
//            request.getRequestURL()，返回IE地址栏地址：http://localhost:8080/dmsd-itoo-exam-log-web/course/index.jsp；
//
//            request.getRequestURI() ，返回包含工程名的当前页面全路径：/dmsd-itoo-exam-log-web/course/index.jsp。
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");//如果没有用户信息，但是访问的路径又是LoginRequired的路径
                return false;
            }
        }
        return true;
    }
}
