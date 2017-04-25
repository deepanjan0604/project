package com.project.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.app.entities.User;

/**
 * It extends JpaRepository to perform CRUD operations
 *
 */
public interface UserRepository extends JpaRepository<User, String> {

	
	User findById(String userid);

	User findByEmail(String email);

	User findByUsername(String userName);


}
