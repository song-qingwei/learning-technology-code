package com.example.security;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * @author SongQingWei
 * @date 2018年7月4日 下午1:44:09
 */
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

	private static final Logger log = LoggerFactory.getLogger(MyAccessDecisionManager.class);
	
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if (null == configAttributes || configAttributes.size() <= 0) {
            return;
        }
        log.info("所请求的资源：{}", object.toString());
        for (ConfigAttribute configAttribute : configAttributes) {
            String needRole = configAttribute.getAttribute();
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                log.info("需要：{}---用户实际权限---{}", needRole, grantedAuthority.getAuthority());
                //grantedAuthority is user's role.
                if (needRole.equals(grantedAuthority.getAuthority())) {
                    log.info("用户取得权限:{}", needRole);
                    return;
                }
            }
        }
        log.info("用户没有权限访问");
        throw new AccessDeniedException("no right");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
