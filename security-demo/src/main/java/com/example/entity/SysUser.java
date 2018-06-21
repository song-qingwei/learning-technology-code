package com.example.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author SongQingWei
 * @description 用户实体
 * @date 2018年05月11 13:09
 */
@Data
public class SysUser {

    private Integer id;

    private String userName;

    private String userPassword;

    private String userEmail;

    private Integer userStatus;

    private Date createTime;
}