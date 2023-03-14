package com.averta.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="role_permissions")
public class RolePermissions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "permission_id", unique = true, nullable = false)	
	private Long permissionId;
	
	@NotBlank(message="Module is missing")
	@Column(name="module",length =100)
	private String module;
	
	@NotBlank(message="Give least One Permission")
	@Column(name="add_permission",length =100)
	private Boolean addPermission;
	
	@Column(name="edit_permission",length =100)
	private Boolean editPermission;
	
	@Column(name="list_permission")
	private Boolean listPermission;
	
	@Column(name="delete_permission")
	private Boolean deletePermission;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private UserRole userRole;

	//**
	
	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Boolean getAddPermission() {
		return addPermission;
	}

	public void setAddPermission(Boolean addPermission) {
		this.addPermission = addPermission;
	}

	public Boolean getEditPermission() {
		return editPermission;
	}

	public void setEditPermission(Boolean editPermission) {
		this.editPermission = editPermission;
	}

	public Boolean getListPermission() {
		return listPermission;
	}

	public void setListPermission(Boolean listPermission) {
		this.listPermission = listPermission;
	}

	public Boolean getDeletePermission() {
		return deletePermission;
	}

	public void setDeletePermission(Boolean deletePermission) {
		this.deletePermission = deletePermission;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
	
	
}
