package com.project.app.auth;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 
 * The default functionality is to skip CSRF checking for GET method. This
 * functionality is lost when an explicit request matcher is provided. So, need
 * to make sure that GET methods are skipped manually.
 *
 */

public class CsrfRequestMatcher implements RequestMatcher {

	// Always allow the HTTP GET method
	private Pattern allowedMethods = Pattern.compile("^GET$");
	private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher(
			"/all", null);

	@Override
	public boolean matches(HttpServletRequest request) {

		// Skip checking if request method is a GET
		if (allowedMethods.matcher(request.getMethod()).matches()) {
			return false;
		}

		// Check CSRF in all other cases.
		return !unprotectedMatcher.matches(request);
	}

}
