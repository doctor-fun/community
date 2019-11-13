package com.newcoder.community.controller;

import com.newcoder.community.model.Comment;
import com.newcoder.community.model.DiscussPost;
import com.newcoder.community.model.Page;
import com.newcoder.community.model.User;
import com.newcoder.community.service.CommentService;
import com.newcoder.community.service.DiscussPostService;
import com.newcoder.community.service.UserService;
import com.newcoder.community.utils.CommunityConstant;
import com.newcoder.community.utils.CommunityUtils;
import com.newcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
 @Autowired
private DiscussPostService discussPostService;
@Autowired
private HostHolder hostHolder;
@Autowired
private UserService userService;
@Autowired
private CommentService commentService;

  @RequestMapping(path="/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
    User user=hostHolder.getUser();
    if(user==null){
        return CommunityUtils.getJSONString(403,"你还没有登录");
    }
    DiscussPost post=new DiscussPost();
    post.setUserId(user.getId());
    post.setTitle(title);
    post.setContent(content);
    post.setCreateTime(new Date());
    discussPostService.addDiscussPost(post);
    //报错的情况，将来统一处理
    return CommunityUtils.getJSONString(0,"发布成功");
  }

  @RequestMapping(path="/detail/{discussPostId}",method =RequestMethod.GET)
  //page会由springmvc生成并注入到model当中
  public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
    //查询帖子
   DiscussPost post= discussPostService.findDiscussPostById(discussPostId);
   model.addAttribute("post",post);
  //查帖子的作者
    User user=userService.findUserById(post.getUserId());
    model.addAttribute("user",user);


    //查这个帖子的评论信息,分页查询
    page.setLimit(5);//每页显示5条
    page.setPath("/discuss/detail/" +discussPostId);
    page.setRows(post.getCommentCount());//帖子下面总的评论行数，用户自动根据这个计算多少页
    //获得当前帖子的评论，每个评论都有一列专门记录它是什么对象（类别）下的评论，是用户的评论还是帖子（ENtity_Type_Post），及评论属于相应类别下哪个实例
    List<Comment> commentList=
    commentService.findCommentsByEntity(ENTITY_TYPE_POST,post.getId(),page.getOffset(),page.getLimit());
   //VO=VIEW OBJECT,用户根据用户id拿到用户头像
    //评论：给帖子的评论
    //回复:给评论的评论
    //帖子的评论列表commentList
    List<Map<String,Object>> commentVoList=new ArrayList<>();
    if(commentList!=null){
      for(Comment comment:commentList){

        //每个评论对应的用户
        Map<String,Object> commentVo=new HashMap<>();
        commentVo.put("comment",comment);
        commentVo.put("user",userService.findUserById(comment.getUserId()));
        //查询回复列表
      List<Comment> replyList= commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,
                comment.getId(),0,Integer.MAX_VALUE);//有多少查多少
        //回复的VO列表
        List<Map<String,Object>> replyVoList=new ArrayList<>();
      if(replyList!=null){
        for(Comment reply :replyList){
          Map<String,Object> replyVo=new HashMap<>();
          //回复
          replyVo.put("reply",reply);
          replyVo.put("user",userService.findUserById(reply.getUserId()));
          User targetUser=  reply.getTargetId()==0?null:userService.findUserById(reply.getTargetId());
          replyVo.put("target",targetUser);
          replyVoList.add(replyVo);
        }
      }
      commentVo.put("replys",replyVoList);
      //某个评论的回复数量
        int replyCount=commentService.findCommentCount(ENTITY_TYPE_COMMENT,comment.getId());
        commentVo.put("replayCount",replyCount);
        commentVoList.add(commentVo);


      }
    }
    model.addAttribute("comments",commentVoList);

    return "/site/discuss-detail";
  }


}
