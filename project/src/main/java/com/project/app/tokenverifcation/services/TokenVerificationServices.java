/**
 * 
 */
package com.project.app.tokenverifcation.services;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.app.entities.User;
import com.project.app.entities.VerificationToken;
import com.project.app.enums.TokenVerify;
import com.project.app.enums.UserEnable;
import com.project.app.repositories.TokenVerificationRepository;
import com.project.app.repositories.UserRepository;
import com.project.app.util.DateUtilities;
import com.project.app.util.DefaultRestException;
import com.project.app.util.MailUtilities;
import com.project.app.util.Utilities;

/**
 * @author deepanjan.mal
 *
 */

@Service
public class TokenVerificationServices {

	private Logger logger = LoggerFactory
			.getLogger(TokenVerificationServices.class);

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@Autowired
	MailUtilities mailUtilities;

	@Autowired
	TokenVerificationRepository tokenVerificationRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * @param email
	 * @param token
	 * @param link
	 * @throws Exception
	 */
	@Async
	public void sendEmail(String email, String link, String token)
			throws Exception {

		String message = TokenVerify.MESSAGE.getMessage(token, link);
		mailUtilities.sendHtmlEmail(email, "Verification of user", message);
	}

	@Async
	public void sendTenantEmail(String email, String username, String link,
			 String token) throws Exception {

		String message = TokenVerify.TENANT_LINK_SUPER.getTenantSuperMessage(
				username, link,  token);
		mailUtilities.sendHtmlEmail(email, "Verification of user", message);
	}

	/**
	 * @param user
	 */
	public Map<String, String> userRegistration(HashMap<Object, Object> userreg, String token, String userId)
			throws Exception {
		
		Map<String, String> map= new HashMap<String, String>(); 
		String password = userreg.get("password").toString();
		User user = userRepository.findById(userId);
		VerificationToken verificationToken = tokenVerificationRepository
				.findByUser(user);
		
		if(verificationToken!=null){
		if (!user.getStatus().equals(UserEnable.INACTIVE)) {
			if (verificationToken.getToken().equals(token)) {
				if (!verificationToken.isVerified()) {
					
					if (DateUtilities.getDateAndTimeDiff(verificationToken.getExpiryDate(), Utilities.getCurrentDateAndTime())) {
						map.put("error","auth message link expired");
						tokenVerificationRepository.delete(verificationToken);
					} else {
						user.setStatus(UserEnable.ACTIVE);
						user.setEnabled(true); // to be configured as users will
												// be set active by admin
						user.setStatus(UserEnable.ACTIVE);
						user.setPassword(passwordEncoder.encode(password));
						userRepository.save(user);
						verificationToken.setToken(null);
						verificationToken.setVerified(true);
						tokenVerificationRepository.save(verificationToken);
						tokenVerificationRepository.delete(verificationToken);
					}
				} else {
					logger.info("User already verified");
				}
			} else {
				logger.info("Token expired or doesnot match");
			}
		} else {
			logger.info("User Inactive");
		}
		map.put("success","success");
	}else{
		map.put("error","Verification token and link expired!! Contact Admin");
	}
		return map;
	}

	/**
	 * 
	 * 
	 * @param token
	 * @param userId
	 * @return 
	 * @throws ParseException 
	 * @throws DefaultRestException 
	 * 
	 * 
	 */
	public Map<String, Object> checkAuthLink(String token, String userId) throws Exception{

		Map<String, Object> map=new HashMap<String, Object>();
		User user = userRepository.findById(userId);
		VerificationToken verificationToken = tokenVerificationRepository
				.findByUser(user);
		if(verificationToken!=null){
			if(DateUtilities.getDateAndTimeDiff(verificationToken.getExpiryDate(), Utilities.getCurrentDateAndTime())){
				map.put("error","Verification token and link expired!! Contact Admin");
				tokenVerificationRepository.delete(verificationToken);
			}
			else
			map.put("success","Verification token and link");
		}
		else{
			map.put("error","Verification token and link expired!! Contact Admin");
			throw new DefaultRestException(map, "Verification token and link expired!! Contact Admin");
		}
		
		return map;
	}
	
}
