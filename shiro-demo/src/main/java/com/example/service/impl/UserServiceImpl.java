package com.example.service.impl;

import com.example.dao.SysUserMapper;
import com.example.entity.SysUser;
import com.example.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author SongQingWei
 * @date 2018年06月06 15:28
 */
@Service(value = "iUserService")
public class UserServiceImpl implements IUserService {

    @Resource
    private SysUserMapper userMapper;

    @Override
    public SysUser findUserByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }
}
