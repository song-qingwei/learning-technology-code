package com.example.service;

import java.util.List;

import com.example.model.Authority;

/**
 * @author SongQingWei
 * @date 2018年7月4日 下午1:27:52
 */
public interface IAuthorityService {
	
	/**
	 * 查询所有权限
	 * @return
	 */
	List<Authority> selectAuthority();
}
