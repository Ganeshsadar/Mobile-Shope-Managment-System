package com.averta.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.averta.inventory.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	public List<UserRole> findByStatus(String status);

}
