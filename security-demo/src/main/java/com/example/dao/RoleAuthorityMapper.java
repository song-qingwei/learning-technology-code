package com.example.dao;

import com.example.model.RoleAuthority;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午10:01:37
 */
public interface RoleAuthorityMapper {

	int deleteByPrimaryKey(Integer id);

    int insert(RoleAuthority record);

    int insertSelective(RoleAuthority record);

    RoleAuthority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleAuthority record);

    int updateByPrimaryKey(RoleAuthority record);
}
