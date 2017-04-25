/**
 * 
 */
package com.project.app.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.app.enums.TokenVerify;

/**
 * @author deepanjan.mal
 *
 */
@Entity
@Table(name = "verification_tokens")
public class VerificationToken {
	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@Column(name = "token")
	private String token;

	@Column(name = "verified")
	boolean verified = false;

	@OneToOne
	private User user;

	@Column(name = "expiry_date")
	private java.util.Date expiryDate;

	@Transient
	private TokenVerify tokenVerify;

	public VerificationToken() {
		super();
	}

	@JsonIgnore
	public VerificationToken(String token, String activationLink, User user) {
		super();
		this.token = token;
		this.user = user;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
		this.verified = false;
	}

	@JsonIgnore
	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}

	/**
	 * @return the tokenVerify
	 */
	@JsonIgnore
	public TokenVerify getTokenVerify() {
		return tokenVerify;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}



	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the verified
	 */
	public boolean isVerified() {
		return verified;
	}

	/**
	 * @param verified
	 *            the verified to set
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the expiryDate
	 */
	public java.util.Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param date
	 *            the expiryDate to set
	 */
	public void setExpiryDate(java.util.Date date) {
		this.expiryDate = date;
	}

	/**
	 * @return the expiration
	 */
	public static int getExpiration() {
		return EXPIRATION;
	}

}