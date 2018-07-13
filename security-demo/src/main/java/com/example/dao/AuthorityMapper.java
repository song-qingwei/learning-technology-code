package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.model.Authority;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午10:02:00
 */
public interface AuthorityMapper {

	int deleteByPrimaryKey(Integer id);

    int insert(Authority record);

    int insertSelective(Authority record);

    Authority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Authority record);

    int updateByPrimaryKey(Authority record);
    
    List<Authority> findByUserId(@Param(value = "userId") Integer userId);
    
    /**
     * 查询所有
     * @return
     */
    List<Authority> findAll();
}
