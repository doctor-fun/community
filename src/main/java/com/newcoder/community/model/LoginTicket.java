package com.newcoder.community.model;

import lombok.Data;

import java.util.Date;

@Data
public class LoginTicket {
    private int  id;
    private int userId;
    private String ticket;
    private int Status;
    private Date expired;


}
