package com.example.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.example.model.Authority;
import com.example.service.IAuthorityService;

/**
 * @author SongQingWei
 * @date 2018年7月4日 下午1:23:52
 */
@Component
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private static final Logger log = LoggerFactory.getLogger(MyInvocationSecurityMetadataSource.class);
	
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	@Resource
    private IAuthorityService iAuthorityService;
	
	/**
     * 系统启动时加载所有需要验证权限的资源定义
     */
    private void loadResourceDefine() {
        log.info("加载需要验证权限的资源定义");
        resourceMap = new HashMap<>(16);
        Collection<ConfigAttribute> attributes;
        ConfigAttribute configAttribute;
        List<Authority> functions = iAuthorityService.selectAuthority();
        for (Authority authority : functions) {
            attributes = new ArrayList<>();
            configAttribute = new SecurityConfig(authority.getName());
            attributes.add(configAttribute);
            resourceMap.put(authority.getUrl(), attributes);
        }
    }
    
    /**
     * * 此方法是为了判定用户请求的url 是否在权限表中
     * 如果在权限表中，则返回给 decide 方法
     * 用来判定用户是否有此权限
     * 如果不在权限表中则放行。
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
