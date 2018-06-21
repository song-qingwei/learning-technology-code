package com.example.dao;

import com.example.entity.SysUserRole;

/**
 * @author SongQingWei
 * @description 用户角色 Dao
 * @date 2018年05月11 13:09
 */
public interface SysUserRoleMapper {

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
    int insert(SysUserRole record);

    /**
     * 动态增加
     * @param record record
     * @return 影响行数
     */
    int insertSelective(SysUserRole record);

    /**
     * 通过主键查询
     * @param id id
     * @return SysUserRole
     */
    SysUserRole selectByPrimaryKey(Integer id);

    /**
     * 动态修改
     * @param record record
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(SysUserRole record);

    /**
     * 修改
     * @param record record
     * @return 影响行数
     */
    int updateByPrimaryKey(SysUserRole record);
}