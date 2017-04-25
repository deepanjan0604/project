/**
 * 
 */
package com.project.app.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author deepanjan.mal
 *
 */
public class TokenVerificationUtiities {

	private static final int EXPIRATION = 60 * 24;

	public static Logger log = LoggerFactory
			.getLogger(TokenVerificationUtiities.class);

	public static String generateToken() {
		return UUID.randomUUID().toString();
	}

	public static String generateLink(String hostName, String token, String userId) {
		String confirmationUrl = hostName + "/activateuser/" + token+"/"+userId+"/"+UUID.randomUUID().toString();
		return confirmationUrl;
	}
	
	
	/**
	 * 
	 * @param hostname
	 * @param token
	 * @param tenantName
	 * @return
	 */
	public static String generateTenantLink(String hostname, String token, String tenantName){
		String tenantUrl = hostname + "/tenant/" + tenantName+"/"+UUID.randomUUID().toString()+token;
		return tenantUrl;
	}

	public static Date calculateExpiryDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, EXPIRATION);
		return new Date(cal.getTime().getTime());
	}

}
