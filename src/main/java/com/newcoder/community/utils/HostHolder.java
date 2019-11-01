package com.newcoder.community.utils;

import com.newcoder.community.model.User;

import org.springframework.stereotype.Component;

/**
 * 持有用户信息
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users=new ThreadLocal<User>();
    public void setUser(User user){
        users.set(user);//将user存到相应线程所对应的map中；
    }
    //     */
    //    public T get() {
    //        Thread t = Thread.currentThread();
    //        ThreadLocalMap map = getMap(t);
    //        if (map != null) {
    //            ThreadLocalMap.Entry e = map.getEntry(this);
    //            if (e != null) {
    //                @SuppressWarnings("unchecked")
    //                T result = (T)e.value;
    //                return result;
    //            }
    //        }
    //        return setInitialValue();
    //    }
    public User getUser(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }

}
