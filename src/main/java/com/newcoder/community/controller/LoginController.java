package com.newcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.newcoder.community.dao.LoginTicketMapper;
import com.newcoder.community.model.LoginTicket;
import org.apache.commons.lang3.StringUtils;
import com.newcoder.community.model.User;
import com.newcoder.community.service.UserService;
import com.newcoder.community.utils.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Map;

@Controller
public class LoginController  implements CommunityConstant {

    //LoginController.class 以LoginController.class的名字作为logger日志的输入类名
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Value("${server.servlet.context-path}")
    private  String contextPath;
    //获取

    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @RequestMapping(path="/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";

    }
    @Autowired
    private Producer kaptchaProducer;

    @RequestMapping(path = "/login",method=RequestMethod.GET)
    public String getLoginPage(){

        return "/site/login";
    }

    @RequestMapping(path="/register",method = RequestMethod.POST)
    //用户填写的信息会根据名称自动的绑定到user的属性当中，而user的初始化是由springmvc处理的
    public String register(Model model, User user){
        Map<String,Object> map= userService.register(user);
        if(map==null ||map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        } else{
            //有错误
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }
    // http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}",method=RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code){
        int result = userService.activation(userId,code);
        if(result== ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账号已经可以正常使用了！");
            model.addAttribute("target","/login");
        }else if (result==ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，该账号已经激活过了！");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败，您提供的激活码不正确");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }
    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession httpSession){
       //生成验证码
        String text=kaptchaProducer.createText();
        BufferedImage image=kaptchaProducer.createImage(text);
        //将验证码存入session
        httpSession.setAttribute("kaptcha",text);
        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os=response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }
    }
    //remember me的意思就记住我,前端点了相应的复选框
    @RequestMapping(path="/login",method = RequestMethod.POST)//页面传进来的信息要用controller去接
    public String login(String username, String password, String code, boolean rememberme, Model model , HttpSession session, HttpServletResponse response) {
        //上面生成验证码的时候有用到httpSession对象，这里是同一个么
        String kapacha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kapacha) || StringUtils.isBlank(code)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "/site/login";
        }
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);//成功会收到类cookie的信息ticket
        if (map.containsKey("ticket")) {
            //放到真正的cookie,cookie的Key value都得是string
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);//cookie在整个应用路径下共享
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            model.addAttribute("user",map.get("user"));
            return "redirect:/index";
        } else
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
        return "/site/login";

    }
    //浏览器保存了cookie
    @RequestMapping(path="/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";//重定向的时候默认是get请求
    }

}
