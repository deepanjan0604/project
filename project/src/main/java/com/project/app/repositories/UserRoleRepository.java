package com.project.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.app.entities.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

	UserRole findByUserId(String id);

}
