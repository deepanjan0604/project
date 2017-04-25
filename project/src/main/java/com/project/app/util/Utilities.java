package com.project.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Common utilities class containing utility methods used across other classes
 * 
 */
@Service
public class Utilities {

	static Logger logger = LoggerFactory.getLogger(Utilities.class);

	public static final SimpleDateFormat SEARCH_DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy");

	/**
	 * A simple null check method. If the given argument is null, then the
	 * method throws an exception and if the argument is not null then it
	 * returns it.
	 * 
	 * @param argument
	 *            - The property which is to be null-checked
	 * @param name
	 *            - The name of the property to be used in the exception message
	 *            (In case it is null)
	 * @return the original object if it is non-null
	 */

	public static <T extends Object> T checkReturn(T argument, String name) {
		if (argument == null) {
			throw new RuntimeException("Property " + name + " is null");
		} else {
			return argument;
		}
	}

	/**
	 * 
	 * Get current system date and time
	 * 
	 * @return
	 * @throws ParseException
	 * 
	 * 
	 */
	public static Date getCurrentDateAndTime() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		logger.info(" Get current date and time " + dateFormat.format(date));
		Date dateTime = dateFormat.parse(dateFormat.format(date));
		return dateTime;
	}

	

}
