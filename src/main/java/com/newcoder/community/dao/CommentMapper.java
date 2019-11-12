package com.newcoder.community.dao;

import com.newcoder.community.model.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    //每个帖子有很多评论，要对这些评论进行分页显示，就需要评论的总数和每页显示多少评论数

    /**
     *
     * @param
     * @param entityId
     * @param offset 每页起始行数（从0行开始数起）
     * @param limit 每页显示的行数的限制
     * @return
     */
    List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit);
    int selectCountByEntity(int entityType,int entityId);
}

