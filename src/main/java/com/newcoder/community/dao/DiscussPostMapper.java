package com.newcoder.community.dao;

import com.newcoder.community.model.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
//    帖子返回的是所有的评论对象,根据用户
    //offset每一页起始行行号
    //limit每一页做多显示的数据
    //一共有多少页=多少数据/limit
    List<DiscussPost> selectDiscussPosts(int userId, int offset,int limit);

//    @Param用于给参数取别名，
    //如果只有1个参数，并且在<if>里使用，那么必须使用，注:sql里有if判断
    //查询特定用户有多少行帖子，用于计算一共有多少页，当为0时查询的是所有用户的值
    int selectDiscussPostRows(@Param("userId")int userId);

}
