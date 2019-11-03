package com.newcoder.community.controller;

import com.newcoder.community.annotation.LoginRequired;
import com.newcoder.community.model.User;
import com.newcoder.community.service.UserService;
import com.newcoder.community.utils.CommunityUtils;
import com.newcoder.community.utils.HostHolder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path="setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
//主要用到MultipartFile这个类，一个是51行的方法，一个是63的方法,获得变量，存到文件里（设置文件名）
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage==null){
            model.addAttribute("error","您还没有选择图片！");
            return "/site/setting";
        }
       String fileName=   headerImage.getOriginalFilename();
       String suffix= fileName.substring(fileName.lastIndexOf("."));
       if(StringUtils.isBlank(suffix)){
           model.addAttribute("error","文件格式不正确");
           return "/site/setting";
       }
       //生成随机文件名
        fileName=CommunityUtils.generateUUID()+suffix;
       //确定文件存放的路径
        File dest=new File(uploadPath+"/",fileName);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
          logger.error("上传文件失败: "+e.getMessage());
          throw new RuntimeException("上传文件失败，服务器发生异常!",e);
        }
        //用户上传了文件头像，就要改变数据库里header字段的内容
        //这里给的应该是一个Url
        //http://localhost:8080/community/user/header/***.png
        User user=hostHolder.getUser();
        String headerUrl=domain+contextPath+"/user/header/"+fileName;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }

    //读取对应的文件内容，先用输入流将文件内容读进来，然后将输入流的东西放到buffer里，放的个数为B,再用输出流将buffer里面的b个字节转出来，交给respons e
    @RequestMapping(path="/header/{fileName}",method=RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName , HttpServletResponse response){
      fileName=  uploadPath+"/"+fileName;
      String suffix=fileName.substring(fileName.lastIndexOf("."));
      response.setContentType("image/"+suffix);
        try {
            OutputStream os =response.getOutputStream();//springmvc会自动关闭response对象创建的输出，httpServletResponse是
//            springmvc管理的
            FileInputStream fis=new FileInputStream(fileName);
            byte[] buffer=new byte[1024];
            int  b=0;//b是读取的字节数
            //void write(byte[] b, int off, int len)
            //将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此输出流。
            while ((b=fis.read(buffer))!=-1) {
                os.write(buffer,0,b);
            }

         } catch (IOException e) {
            logger.error("读取头像失败" + e.getMessage());
        }


    }

   @RequestMapping(value = "/transpwd",method = RequestMethod.POST)
   public String transferPassword(String oldPassword,String newPassword,String confirmNewPassword,Model model){
        User user = hostHolder.getUser();
        Map<String,Object> map=userService.transferPassword(user.getUsername(),oldPassword,newPassword,confirmNewPassword);
        if(map.containsKey("oldPasswordMsg")){
            model.addAttribute("oldPasswordMsg",map.get("oldPasswordMsg"));
        }else if(map.containsKey("confirmPasswordMsg")){
            model.addAttribute("confirmPasswordMsg",map.get("confirmPasswordMsg"));
       }
        else if(map.containsKey("successMsg")){
            model.addAttribute("successMsg",map.get("successMsg"));
        }
        return "/site/setting";
  
   }

}

