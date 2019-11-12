package com.newcoder.community.service;

import com.newcoder.community.dao.DiscussPostMapper;
import com.newcoder.community.dao.UserMapper;
import com.newcoder.community.model.DiscussPost;
import com.newcoder.community.model.User;
import com.newcoder.community.utils.CommunityUtils;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

@Service
public class AlphaService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    /**
     * 事务的练习
     * @return
     *
     *
     *   REQUIRED(0),
     *
     *
     *     REQUIRES_NEW(3),
     *
     *     NESTED(6);
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Object save1(){

        //新增用户
        User user=new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtils.generateUUID().substring(0,5));
        user.setPassword(CommunityUtils.md5("123"+user.getSalt()));

        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //新增帖子
        DiscussPost post=new DiscussPost();
        post.setUserId(user.getId());//userId由数据库生成，再回填回user这个实例，此时已经有userId
        post.setTitle("hello");
        post.setContent("信任报道");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        return "Ok";
    }

    @Autowired
    private TransactionTemplate transactionTemplate;
    /**
     * 编程式事务举例
     */
    public Object save2(){
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            //回调方法，由transactionTemplate自动调这个doInTransaction方法，Object返回的泛型是TransactionCallback的泛型<Object>
            //这个方法执行的是业务逻辑,将业务逻辑交给模板进行事务管理，隔离级别和传播级别自定义设置

            public Object doInTransaction(TransactionStatus transactionStatus) {

                //新增用户
                User user=new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtils.generateUUID().substring(0,5));
                user.setPassword(CommunityUtils.md5("123"+user.getSalt()));

                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/999.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                //新增帖子
                DiscussPost post=new DiscussPost();
                post.setUserId(user.getId());//userId由数据库生成，再回填回user这个实例，此时已经有userId
                post.setTitle("您好");
                post.setContent("我是新人");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                return "Ok";
            }
        });
        
    }

}
