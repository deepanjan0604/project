/**
 * 
 */
package com.project.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.entities.User;
import com.project.app.entities.VerificationToken;

/**
 * @author deepanjan.mal
 *
 */
@Repository
public interface TokenVerificationRepository extends JpaRepository<VerificationToken, Integer> {

	VerificationToken findByUser(User user);
}
