package com.example.dao;

import com.example.model.Role;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午10:01:05
 */
public interface RoleMapper {

	int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}
