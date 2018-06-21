package com.example.dao;

import com.example.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * @author SongQingWei
 * @description 用户 Dao
 * @date 2018年05月11 13:09
 */
public interface SysUserMapper {

    /**
     * 通过主键删除
     * @param id id
     * @return 影响行数
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 增加
     * @param record record
     * @return 影响行数
     */
    int insert(SysUser record);

    /**
     * 动态增加
     * @param record record
     * @return 影响行数
     */
    int insertSelective(SysUser record);

    /**
     * 通过主键查询
     * @param id id
     * @return SysUser
     */
    SysUser selectByPrimaryKey(Integer id);

    /**
     * 动态修改
     * @param record record
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(SysUser record);

    /**
     * 修改
     * @param record record
     * @return 影响行数
     */
    int updateByPrimaryKey(SysUser record);

    /**
     * 通过用户名查询用户信息
     * @param userName 用户名
     * @return user
     */
    SysUser selectByUserName(@Param("userName") String userName);
}