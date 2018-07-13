package com.example.model;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午9:59:13
 */
public class RoleAuthority {

	private Integer id;

    private Integer roleId;

    private Integer authorityId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Integer authorityId) {
		this.authorityId = authorityId;
	}
}
