package com.example.entity;

import lombok.Data;

/**
 * @author SongQingWei
 * @description 用户角色关系实体
 * @date 2018年05月11 13:09
 */
@Data
public class SysUserRole {

    private Integer id;

    private Integer userId;

    private Integer roleId;
}