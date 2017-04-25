
package com.project.app.tokenverifcation.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.tokenverifcation.services.TokenVerificationServices;

/**
 * @author deepanjan.mal@darkhorseboa.com
 *
 */
@RestController
public class TokenVerificationController {

	public final Logger logger = LoggerFactory
			.getLogger(TokenVerificationController.class);

	@Autowired
	TokenVerificationServices tokenVerificationServices;

	@RequestMapping("/public/registration")
	public void userRegistration(@RequestBody HashMap<Object, Object> user,
			@RequestParam String token, @RequestParam("user") String userId) {
		try {
			tokenVerificationServices.userRegistration(user, token, userId);
		} catch (Exception e) {
			logger.info("Exception", e);
		}
	}
	
	
	@RequestMapping("/public/check/link")
	public Map<String , Object> checkAuthLink(
			@RequestParam String token, @RequestParam("user") String userId) throws Exception{
	
		return	tokenVerificationServices.checkAuthLink(token, userId);
		
	
	}
}
