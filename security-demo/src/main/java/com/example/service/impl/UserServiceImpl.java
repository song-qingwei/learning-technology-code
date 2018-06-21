package com.example.service.impl;

import com.example.dao.SysPermissionMapper;
import com.example.dao.SysUserMapper;
import com.example.entity.SysPermission;
import com.example.entity.SysUser;
import com.example.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SongQingWei
 * @date 2018年06月06 10:33
 */
@Service(value = "iUserService")
@Slf4j
public class UserServiceImpl implements IUserService, UserDetailsService {

    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysPermissionMapper permissionMapper;

    @Override
    public SysUser getUserByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("【登录】用户名:{}", username);
        SysUser sysUser = userMapper.selectByUserName(username);
        if (sysUser != null) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            List<SysPermission> permissions = permissionMapper.findPermissionByUserId(sysUser.getId());
            log.info("{}", permissions);
            if (permissions != null && permissions.size() > 0) {
                for (SysPermission permission : permissions) {
                    // 此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用 GrantedAuthority 对象。
                    GrantedAuthority authority = new SimpleGrantedAuthority(permission.getPermissionName());
                    log.info("【登录】用户权限:{}", authority);
                    grantedAuthorities.add(authority);
                }
            }
            return new User(sysUser.getUserName(), sysUser.getUserPassword(), true, true, true, true, grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("User not found for name:" + username);
        }
    }
}
