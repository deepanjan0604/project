package com.project.app.util;

import java.util.Map;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class DefaultRestException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5109174745239470110L;
	private String message;
	private Map<String, Object> map;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	/**
	 * @param message
	 * @param map
	 */
	public DefaultRestException(Map<String, Object> map, String message) {
		super();
		this.message = message;
		this.map = map;
	}

}
