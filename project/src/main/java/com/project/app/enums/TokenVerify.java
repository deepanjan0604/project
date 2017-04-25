/**
 * 
 */
package com.project.app.enums;

import org.apache.commons.lang3.text.StrBuilder;

/**
 * @author deepanjan.mal@darkhorseboa.com
 *
 */
public enum TokenVerify {

	MESSAGE("<br>To set password click on this url :" + "<a href='" + "link"
			+ "'>" + "Click here" + "</a>"),

	TENANT_LINK("Company admin credentials and url"
			+ "<br>Credentials for Admin " + "<br>Username: #username"
			+ "<br>Password: #password"
			+ "<br>Share this url with other users: #link"),

	TENANT_LINK_SUPER("Company admin credentials and url"
			+ "<br>Username: #username"
			+ "<br>Share this url with other users: #link");
		/*	+ "<br>To set password click on this url :" + "<a href='" + "link1"
			+ "'>" + "Click here" + "</a>");
*/
	/**
	 * @param template
	 */
	private TokenVerify(String template) {
		this.template = template;
	}

	String template;

	/**
	 * Generate a mail template for activation of a new user with activation
	 * link and reset password with forgot password link
	 * 
	 * @param token
	 * @param link
	 * @return String
	 */
	public String getMessage(String token, String link) {
		return new StrBuilder(template).replaceAll("link", link.toString())
				.toString();
	}

	/**
	 * Generates a mail template for new tenant registration via signup page.
	 * 
	 * @param username
	 * @param password
	 * @param link
	 * @return String
	 */

	public String getTenantMessage(String username, String password, String link) {
		return new StrBuilder(template).replaceAll("#username", username)
				.replaceAll("#password", password).replaceAll("#link", link)
				.toString();
	}

	/**
	 * Generates a mail template for new tenant registration when tenant
	 * registration done by super admin
	 * 
	 * 
	 * @param username
	 * @param adminActivationLink
	 * @param addUsersToTenantLink
	 * @param token
	 * @return String
	 */
	public String getTenantSuperMessage(String username, 
			String addUsersToTenantLink, String token) {
		return new StrBuilder(template).replaceAll("#username", username)		
				.replaceAll("#link", addUsersToTenantLink).toString();
	}

}
