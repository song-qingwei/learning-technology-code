package com.example.service;

import com.example.entity.SysUser;

/**
 * @author SongQingWei
 * @date 2018年06月06 10:32
 */
public interface IUserService {

    /**
     * 通过用户名查询
     * @param userName 用户名
     * @return user
     */
    SysUser getUserByUserName(String userName);
}
