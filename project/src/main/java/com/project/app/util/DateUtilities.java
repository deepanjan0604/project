package com.project.app.util;

/*
 * deepanjan.mal@darkhorseboa.com
 * 
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtilities {
	public static Logger logger = LoggerFactory.getLogger(DateUtilities.class);

	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	public static Calendar cal = Calendar.getInstance();

	/**
	 * 
	 * truncate the date to a specified format irrespective of time
	 * 
	 * @return
	 * @throws ParseException
	 * 
	 * 
	 */
	public static String truncateAuditDate() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		logger.info(" Get current date:" + dateFormat.format(date));
		String currentDate = dateFormat.format(date);
		return currentDate;
	}

	public static String generateAuditDate(Date date) {

		String dateFormat = null;
		try {
			if (DateUtilities.truncateAuditDate().compareTo(
					DateUtilities.extractDate(date)) == 0) {
				dateFormat = "Today @"
						+ DateUtilities.getTimeFormatForCurrentDate(date);
				logger.info("Current date");
			} else if (DateUtilities.truncateAuditDate().compareTo(
					DateUtilities.extractDate(date)) > 0) {
				if (DateUtilities.getYesterdayDateString().equals(
						DateUtilities.extractDate(date))) {
					logger.info("Yesterday's date:"
							+ DateUtilities.getSpecifiedDateFormat(date));
					dateFormat = "Yesterday";
				} else {
					dateFormat = DateUtilities.getSpecifiedDateFormat(date);
					logger.info("Specfied date format");
				}
			} else {
				dateFormat = "Upcoming("
						+ DateUtilities.getSpecifiedDateFormat(date) + ")";
				logger.info("Specfied date format");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dateFormat;
	}

	/**
	 * 
	 * returns the time in HH:mm format
	 * 
	 * @param date
	 * @return
	 */

	private static String getTimeFormatForCurrentDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		// logger.info("Returning the specified date format");
		String dateformat = sdf.format(date.getTime());
		return dateformat;

	}

	/**
	 * 
	 * returns the Yesterday's date respect to current date and time
	 * 
	 * @param date
	 * @return
	 */

	private static String getYesterdayDateString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		dateFormat.format(cal.getTime());
		return dateFormat.format(cal.getTime());
	}

	/**
	 * 
	 * Returns the date in dd-MMM-yyyy format
	 * 
	 * @param date
	 * @return
	 */

	private static String getSpecifiedDateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String dateFormat = sdf.format(date);
		return dateFormat;
	}

	/**
	 * 
	 * returns the date in yyyy-MM-dd format
	 * 
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 * 
	 */
	private static String extractDate(Date date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// logger.info(" Get date from date and time " +
		// dateFormat.format(date));
		String currentDate = dateFormat.format(date);
		return currentDate;
	}

	/**
	 * Calculate 24 hours difference for two specific dates i.e. startDate and
	 * endDate.
	 * 
	 * It return true if it has crossed 24 hours.
	 * 
	 * It returns false if the difference is less than 24 hours.
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public static boolean getDateAndTimeDiff(Date startDate, Date endDate) {
		boolean status = false;

		long diff = endDate.getTime() - startDate.getTime();
		if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 1) {
			status = true;
		}

		return status;

	}

}
