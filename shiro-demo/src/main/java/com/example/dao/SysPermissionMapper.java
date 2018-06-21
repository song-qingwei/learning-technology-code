package com.example.dao;

import com.example.entity.SysPermission;

/**
 * @author SongQingWei
 * @description 权限 Dao
 * @date 2018年05月11 13:09
 */
public interface SysPermissionMapper {

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
    int insert(SysPermission record);

    /**
     * 动态增加
     * @param record record
     * @return 影响行数
     */
    int insertSelective(SysPermission record);

    /**
     * 通过主键查询
     * @param id id
     * @return SysPermission
     */
    SysPermission selectByPrimaryKey(Integer id);

    /**
     * 动态修改
     * @param record record
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(SysPermission record);

    /**
     * 修改
     * @param record record
     * @return 影响行数
     */
    int updateByPrimaryKey(SysPermission record);
}