package com.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Calendar util
 * @author christ
 *
 */
public class CalendarUtil {

	public static Calendar getLastSunday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK) - 1));
		return cal;
	}
	
	public static Calendar getNextSaturday(Date lastSunday) {
		Calendar saturdayCalendar = Calendar.getInstance();
		saturdayCalendar.setTime(lastSunday);
		saturdayCalendar.add(Calendar.DAY_OF_WEEK, 6);
		return saturdayCalendar;
	}
	
	public static boolean isWeekend(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
//		System.out.println("WeekDay:" + day);
		if (day == 0 || day == 6) {
			return true;
		}

		return false;
	}
	
}
