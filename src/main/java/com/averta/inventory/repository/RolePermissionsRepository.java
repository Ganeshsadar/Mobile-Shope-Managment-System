package com.averta.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.averta.inventory.entity.RolePermissions;

public interface RolePermissionsRepository extends JpaRepository<RolePermissions, Long> {

}
