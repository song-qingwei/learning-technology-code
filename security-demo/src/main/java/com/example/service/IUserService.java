package com.example.service;

import com.example.model.User;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午10:08:42
 */
public interface IUserService {

	User findByUserName(String userName);
	
	void updateSysUserLastLoginTime(User user);
}
