package com.example.dao;

import org.apache.ibatis.annotations.Param;

import com.example.model.User;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午10:00:34
 */
public interface UserMapper {

	int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User findByUserName(@Param(value = "userName") String userName);
}
