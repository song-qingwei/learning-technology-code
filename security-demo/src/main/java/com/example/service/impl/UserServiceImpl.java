package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.dao.AuthorityMapper;
import com.example.dao.UserMapper;
import com.example.model.Authority;
import com.example.model.User;
import com.example.service.IUserService;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午10:09:46
 */
@Service(value = "iUserService")
public class UserServiceImpl implements IUserService, UserDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Resource
	private UserMapper userMapper;
	@Resource
	private AuthorityMapper authorityMapper;
	
	@Override
	public User findByUserName(String userName) {
		return userMapper.findByUserName(userName);
	}
	
	@Override
	public void updateSysUserLastLoginTime(User user) {
		userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("【登录】用户名:{}", username);
		boolean enabled = false;
		User user = this.findByUserName(username);
		if (user != null) {
			if (user.getStatus() == 0) {
				enabled = true;
			}
			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			List<Authority> list = authorityMapper.findByUserId(user.getId());
			if (CollectionUtils.isNotEmpty(list)) {
				for (Authority authority : list) {
					GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
					grantedAuthorities.add(grantedAuthority);
				}
			}
			return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(), enabled, true, true, true, grantedAuthorities);
		} else {
			throw new UsernameNotFoundException("User not found for name:" + username);
		}
	}
}
