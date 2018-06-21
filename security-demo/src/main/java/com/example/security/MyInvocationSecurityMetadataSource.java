package com.example.security;

import com.example.dao.SysPermissionMapper;
import com.example.entity.SysPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author SongQingWei
 * @date 2018年06月06 10:40
 */
@Service
@Slf4j
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    @Resource
    private SysPermissionMapper permissionMapper;

    /**
     * 系统启动时加载所有需要验证权限的资源定义
     */
    private void loadResourceDefine() {
        log.info("加载需要验证权限的资源定义");
        resourceMap = new HashMap<>(16);
        Collection<ConfigAttribute> attributes;
        ConfigAttribute configAttribute;
        List<SysPermission> permissions = permissionMapper.findAll();
        for (SysPermission permission : permissions) {
            attributes = new ArrayList<>();
            configAttribute = new SecurityConfig(permission.getPermissionName());
            attributes.add(configAttribute);
            resourceMap.put(permission.getPermissionUrl(), attributes);
        }
    }

    /**
     * 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
     *
     * @param object object is a URL.
     * @return Collection<ConfigAttribute>
     * @throws IllegalArgumentException e
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (resourceMap == null) {
            loadResourceDefine();
        }
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        AntPathRequestMatcher matcher;
        for (String resultURL : resourceMap.keySet()) {
            matcher = new AntPathRequestMatcher(resultURL);
            if (matcher.matches(request)) {
                return resourceMap.get(resultURL);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
