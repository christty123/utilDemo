package com.util.date;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateCalcUtil {
	//private final static DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static Date RemoveTimePart(Date date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.parse(dateFormat.format(date));
	}

	public static Date parseDateTime(String inputDate) throws Exception {
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return timeFormat.parse(inputDate);
	}

	public static Date parseDate(String inputDate) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.parse(inputDate);
	}

	public static String formatDate(Date date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public static String formatDate(Date date, DateFormat format) {

		if (format == null)
			return null;
		return format.format(date);
	}

	public static String formatDatetime(Date date) {
		DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatDate(date, timeFormat);

	}

	public static Timestamp parseTimestamp(String inputDate) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(inputDate));
		return new Timestamp(calendar.getTimeInMillis());
	}

	public static String getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return new Integer(calendar.get(Calendar.DAY_OF_WEEK)).toString();
	}

	public static Integer getHourOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return new Integer(calendar.get(Calendar.HOUR_OF_DAY));
	}

	public static String getHourAndMinuteOfDay(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		String time = dateFormat.format(date);
		return time;
	}

	public static Date getNearDay(Date date, int offset) {
		if (offset == 0)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int target = offset;
		int step = 1;
		if (target < 0) {
			target *= -1;
			step = -1;
		}
		while (target > 0) {
			calendar.add(Calendar.DAY_OF_YEAR, step);
			target--;
		}
		return calendar.getTime();
	}

	public static Date getNearDateTime(Date date, int offset, int type) {
		if (offset == 0)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int target = offset;
		int step = 1;
		if (target < 0) {
			target *= -1;
			step = -1;
		}
		while (target > 0) {
			calendar.add(type, step);
			target--;
		}
		return calendar.getTime();
	}

	public static int GetDateDifferenceByType(Date date1, Date date2, int CalendarType) {
		Date earlier;
		Date later;
		Boolean switched = false;
		Calendar calendarEarlier = Calendar.getInstance();
		Calendar calendarLater = Calendar.getInstance();
		if (date1.equals(date2)) {
			return 0;
		}
		if (date1.after(date2)) {
			earlier = date2;
			later = date1;
			switched = true;
		} else {
			earlier = date1;
			later = date2;
			switched = false;
		}
		calendarEarlier.setTime(earlier);
		calendarLater.setTime(later);
		int count = 0;
		while (calendarEarlier.before(calendarLater)) {
			calendarEarlier.add(CalendarType, 1);
			count++;
		}
		if (switched == true) {
			count *= -1;
		}
		return count;
	}

	public static Date GetNextHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		return calendar.getTime();
	}

	public static Date GetEndTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, 59);
		calendar.add(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date getDatePart(Date date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormat.format(date);
		return dateFormat.parse(dateStr);
	}

	public static Date getBeginDateTime(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date getEndDateTime(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date getNextHalfHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, 30);
		return calendar.getTime();
	}

	public static String getLastHalfHourStr(String dateTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = format.parse(dateTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, -30);
		return format.format(calendar.getTime());
	}

	public static String getNextHalfHourStr(String dateTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = format.parse(dateTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, 30);
		return format.format(calendar.getTime());
	}
	/***
	 * get currentDate firstDate and last Date
	 * @param currentDate
	 * @return list<date>
	 */
	public static List<Date> getDates(Date currentDate) {
		List<Date> dates=new ArrayList();
		if(null!=currentDate){
			Calendar frist = Calendar.getInstance();
			frist.setTime(currentDate);
			frist.add(Calendar.MONTH, 0);
			frist.set(Calendar.DAY_OF_MONTH, 1);
			dates.add(frist.getTime());
			 //set current month one day
			Calendar last = Calendar.getInstance();
			last.setTime(currentDate);
			//set current month last day
			last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(Calendar.DAY_OF_MONTH));
			dates.add(last.getTime());
		}
		return dates;
	}
	
	/***
	 * get list<date> between  startDate and  endDate  
	 * @param currentDate
	 * @return list<date>
	 */
	public static List<Date> getDates(Date startDate,Date endDate) {
		List<Date> dates=new ArrayList();
		if(null!=startDate&&null!=endDate){
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
//			c.add(Calendar.MONTH, 0);
//			c.set(Calendar.DAY_OF_MONTH, 1);
			// set current month one day
			Calendar ca = Calendar.getInstance();
			ca.setTime(endDate);
			//set current month last day
			//ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
			while (true) {
				if (c.getTimeInMillis() <= ca.getTimeInMillis()) {// TODO
					dates.add(c.getTime());
				} else {
					break;
				}
				c.add(Calendar.DATE, 1);
			}
		}
		
		return dates;
	}

	public static String previousMonthFirstDay(){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	       String firstDay = "";
	        //获取前月的第一天
	        Calendar   cal_1=Calendar.getInstance();//获取当前日期
	        cal_1.add(Calendar.MONTH, -1);
	        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
	        firstDay = dateFormat.format(cal_1.getTime());
	        return firstDay;
	}
	
	public static String thisMonthFirstDayStr(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       String firstDay = "";
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        firstDay = dateFormat.format(cal_1.getTime());
        return firstDay;
	}
	public static String previousMonthLastDay(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  String lastDay = "";
	        //获取前月的最后一天
	        Calendar cale = Calendar.getInstance();  
	        cale.setTime(date);
	        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天
	        lastDay = dateFormat.format(cale.getTime());
	        return lastDay;
	}
	public static String previousMonthLastDay(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  String lastDay = "";
	        //获取前月的最后一天
	        Calendar cale = Calendar.getInstance();  
	        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天
	        lastDay = dateFormat.format(cale.getTime());
	        return lastDay;
	}
	
	public static String NextMonthFirstDay(String dateString) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(dateString);
       String firstDay = "";
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.setTime(date);
        cal_1.add(Calendar.MONTH, 1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        firstDay = dateFormat.format(cal_1.getTime());
        return firstDay;
	}

	public static String NextMonthLastDay(String dateString) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(dateString);
		  String lastDay = "";
	        //获取前月的最后一天
	        Calendar cale = Calendar.getInstance();  
	        cale.setTime(date);
	        cale.add(Calendar.MONTH, 2);
	        cale.set(Calendar.DAY_OF_MONTH,0);
	        lastDay = dateFormat.format(cale.getTime());
	        return lastDay;
	}

	/**
	 * 获取今天开始时间
	 * @return
	 */
	public static final Date getTimeStartToday(){
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    return calendar.getTime();
	}
	
	/**
	 * 获取今天结束时间
	 * @return
	 */
	public static final Date getTodayEndingTime(){
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.add(Calendar.DAY_OF_MONTH, 1);
	    calendar.add(Calendar.SECOND, -1);
	    return calendar.getTime();
	}
	
	/** 
     * 得到几天前的时间 
     *  
     * @param d 
     * @param day 
     * @return 
     */  
    public static final String getDateBefore(Date d, int day) {  
    	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return timeFormat.format(now.getTime());  
    }    
    /** 
     * 得到几天后的时间 
     *  
     * @param d 
     * @param day 
     * @return 
     */  
    public static final String getDateAfter(Date d, int day) {  
    	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);  
        return timeFormat.format(now.getTime());  
        
    }
	/***
	 * get current date past  xx month  time
	 * @param d
	 * @param month
	 * @return 
	 */
    public static final Date getDateBeforeMonth(Date d, int month) {  
    	Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例  
		ca.setTime(d); // 设置时间为当前时间  
		ca.add(Calendar.MONTH, -6);// 月份减6 
		return ca.getTime();
    }   
	
    public static Date  thisMonthFirstDay(){
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
//        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        return cal_1.getTime();
}
    public static Date  thisMonthLastDay(){
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, 1);  
        cal_1.set(Calendar.DAY_OF_MONTH, 0); 
        return cal_1.getTime();
}
    public static int  getMonthFirstDay(){
        Calendar   cal_1=Calendar.getInstance();
        cal_1.set(Calendar.DAY_OF_MONTH,1);
        return cal_1.get(Calendar.DAY_OF_MONTH);
}
    public static int  getMonthLastDay(){
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, 1);  
        cal_1.set(Calendar.DAY_OF_MONTH, 0); 
        return  cal_1.get(Calendar.DAY_OF_MONTH);
}
    
    /**
     * 将数字月份转换成英文简写
     * @param month
     * @return
     */
    public static String numberConversionMonth(String month){
    	
		try {
		    SimpleDateFormat sdf = new SimpleDateFormat("MM");
			Date date = sdf.parse(month);
			sdf = new SimpleDateFormat("MMM",Locale.US);
		    return sdf.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return month;
	    
    }
    
	public static void main(String[] args) {
	}
	public static final Date getDateAddMins(Date d, int mins) 
	{  
	    	Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例  
			ca.setTime(d); // 设置时间为当前时间  
			ca.add(Calendar.MINUTE, mins);// 月份减6 
			return ca.getTime();
	  }  
	/**
	 * 减去多少分钟
	 * @param d
	 * @param mins
	 * @return
	 */
	public static final Date getDateLessMins(Date d, int mins) 
	{  
	    	Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例  
			ca.setTime(d); // 设置时间为当前时间  
			ca.add(Calendar.MINUTE, -mins);// 月份减6 
			return ca.getTime();
	  }  
	public static String getMonthStartDate(String yyyyMM) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Date date = sdf.parse(yyyyMM);
		// Instance Calendar  
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);
		// set Calendar to this Month's first day
		calendar.set(Calendar.DATE, 1);  
		return format.format(calendar.getTime());
	}
	public static String getMonthEndDate(String yyyyMM) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Date date = sdf.parse(yyyyMM);
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);
		// set Calendar to next Month's  
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);  
		// set Calendar to next Month's first day
		calendar.set(Calendar.DATE, 1);  
		calendar.add(Calendar.DATE, -1);  
		return format.format(calendar.getTime());
	}
	
	public static String formatDateNoDivide(Date date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(date);
	}
	
	/**
	 * 设置日期为某个整点时间
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date setDateHour(Date date, int hour){
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, hour);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
}
