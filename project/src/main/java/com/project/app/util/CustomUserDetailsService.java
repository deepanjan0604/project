package com.project.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.app.entities.User;
import com.project.app.repositories.UserRepository;

/**
 * Custom User Details Service. Service responsible to authenticate the user and
 * populate the current logged in user details.
 *
 *
 * It extracts the username and tenant domain from the loginId
 *
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public User loadUserByUsername(String loginId) {

		User user = userRepository.findByUsername(loginId);
		if (user == null) {
			logger.info("User not found in the database. Returning exception");
			throw new UsernameNotFoundException("User does not exist.");
		}
		return user;
	}

}