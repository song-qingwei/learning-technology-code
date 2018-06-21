package com.example.dao;

import com.example.entity.SysPermissionRole;

/**
 * @author SongQingWei
 * @description 权限角色 Dao
 * @date 2018年05月11 13:09
 */
public interface SysPermissionRoleMapper {

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
    int insert(SysPermissionRole record);

    /**
     * 动态增加
     * @param record record
     * @return 影响行数
     */
    int insertSelective(SysPermissionRole record);

    /**
     * 通过主键查询
     * @param id id
     * @return SysPermissionRole
     */
    SysPermissionRole selectByPrimaryKey(Integer id);

    /**
     * 动态修改
     * @param record record
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(SysPermissionRole record);

    /**
     * 动态修改
     * @param record record
     * @return 影响行数
     */
    int updateByPrimaryKey(SysPermissionRole record);
}