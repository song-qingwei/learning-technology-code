/**
 * 
 */
package com.example.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.dao.AuthorityMapper;
import com.example.model.Authority;
import com.example.service.IAuthorityService;

/**
 * @author SongQingWei
 * @date 2018年7月4日 下午1:36:37
 */
@Service
public class AuthorityServiceImpl implements IAuthorityService {

	@Resource
	private AuthorityMapper authorityMapper;
	
	@Override
	public List<Authority> selectAuthority() {
		return authorityMapper.findAll();
	}

}
