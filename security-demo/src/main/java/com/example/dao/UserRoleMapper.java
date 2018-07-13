package com.example.dao;

import com.example.model.UserRole;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午10:02:28
 */
public interface UserRoleMapper {

	int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);
}
