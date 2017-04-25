package com.project.app.util;

/**
 * @author deepanjan.mal@darkhorseboa.com
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.app.entities.User;

/**
 * Utilites service related to authentication/authroization. This service will
 * be autowired in all the other services wherever information related to user
 * details are needed.
 * 
 * @author Pavan
 *
 */
@Service
public class AuthUtilService {

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Returns the tenant of the logged in user
	 * 
	 * @return
	 */
	public User getLoggedInUser() {
		logger.info("Returning the logged in user object");
		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		return user;
	}



	

}
