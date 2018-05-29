package com.DO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeConverter {
	private static Logger logger = LogManager.getLogger(TimeConverter.class);

	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss zzz");

	public static long getEpochTimeInSeconds(String dateString, String timeString, String timeZone) {

		Date date = getDate(dateString, timeString, timeZone);
		long epoch = date != null ? date.getTime() : -1;
		logger.info(epoch);
		return epoch/1000;
	}

	public static Date getDate(String dateString, String timeString,String timeZone) {

		Date date = null;
		try {
			logger.debug("dateString {} , timeString, {}, timeZone {}", dateString, timeString, timeZone);
			date = simpleDateFormat.parse(dateString.trim() + " " + timeString.trim()+" " + timeZone.trim());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug(date);
		return date;
	}

}
