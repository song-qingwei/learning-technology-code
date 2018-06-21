package com.example.shiro;

import com.example.commons.Constants;
import com.example.entity.SysPermission;
import com.example.entity.SysRole;
import com.example.entity.SysUser;
import com.example.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SongQingWei
 * @date 2018年06月06 15:25
 */
@Slf4j
public class AuthRealm extends AuthorizingRealm {

    @Resource
    private IUserService userService;

    /**
     * 授权
     * @param principalCollection principalCollection
     * @return authorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前登录的用户名,等价于(String)principals.fromRealm(this.getName()).iterator().next()
        String currentUsername = (String)super.getAvailablePrincipal(principalCollection);
        SysUser user = userService.findUserByUserName(currentUsername);
        List<String> roles = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        List<SysRole> roleList = user.getRoles();
        if(roleList.size()>0) {
            for(SysRole role : roleList) {
                roles.add(role.getRoleName());
                List<SysPermission> permissionList = role.getPermissions();
                if(permissionList.size()>0) {
                    for(SysPermission permission : permissionList) {
                        permissions.add(permission.getPermissionUrl());
                    }
                }
            }
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //将角色放入 shiro 中.
        simpleAuthorizationInfo.addRoles(roles);
        //将权限放入 shiro 中.
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证登录
     * @param authenticationToken token
     * @return authenticationInfo
     * @throws AuthenticationException e
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取基于用户名和密码的令牌
        //实际上这个 authenticationToken 是从 LoginController 里面 currentUser.login(token) 传过来的
        //两个 token 的引用都是一样的,本例中是 org.apache.shiro.authc.UsernamePasswordToken
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        log.info("验证当前Subject时获取到token为{}", ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
        SysUser user = userService.findUserByUserName(token.getUsername());
        if(null != user){
            AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getUserPassword(), this.getClass().getName());
            this.setSession(user);
            return authenticationInfo;
        }else{
            return null;
        }
    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     * 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
     */
    private void setSession(Object value){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser){
            Session session = currentUser.getSession();
            System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            session.setAttribute(Constants.CURRENT_USER, value);
        }
    }
}
