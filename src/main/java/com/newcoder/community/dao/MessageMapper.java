package com.newcoder.community.dao;

import com.newcoder.community.model.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<Message> selectConversations(int userId,int offset,int limit);
}
