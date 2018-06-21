package com.example.service;

import com.example.entity.SysUser;

/**
 * @author SongQingWei
 * @date 2018年06月06 15:26
 */
public interface IUserService {

    /**
     * 通过用户名查询用户的角色和权限
     * @param userName 用户名
     * @return user
     */
    SysUser findUserByUserName(String userName);
}
