package com.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

public class DateConvertUtil {
	public static Date d = new Date();
	//public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String defaultformat = "yyyy/MM/dd";
	public static String defaultformatf = "dd/MM/yyyy";

	public static String getStrFromDate(Date d) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = df.format(d);
		return strDate;
	}

	public static String getYMDFormatDate(Date d) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (d == null)
			return null;
		return df.format(d);
	}
	
	public static Date getDate(Date d,String format) {
		DateFormat df = new SimpleDateFormat(format);
		if (d == null)
			return null;
		return  parseString2Date(df.format(d), format);
	}


	/**
	 * @author Junfeng_Yan
	 * @param d
	 * @param format
	 *            such as "yyyy-MM-dd or yyyy/MM/dd"
	 * @return
	 */
	public static String date2String(Date d, String format) {
		if (null == d)
			return "";
		if (StringUtils.isEmpty(format))
			format = defaultformat;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(d);
	}

	public static Date getDateFromStr(String strDate) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author Vineela_Jyothi This method returns the no.of days/weeks/months
	 *         between the given date and today. If the given date is after
	 *         today and no.of days in between <30, returns --> 'xxd left' (xx -
	 *         no.of days) If the given date is after today and no.of days in
	 *         between >30 & <90, returns --> 'xxw left' (xx - no.of weeks) If
	 *         the given date is after today and no.of days in between >90,
	 *         returns --> 'xxm left' (xx - no.of months) If the given date is
	 *         before today and no.of days in between <30, returns --> 'xxd ago'
	 *         (xx - no.of days) If the given date is before today and no.of
	 *         days in between >30 & <90, returns --> 'xxw ago' (xx - no.of
	 *         weeks) If the given date is before today and no.of days in
	 *         between >90, returns --> 'xxm ago' (xx - no.of months) Else
	 *         returns (Today)
	 * @param givenDate
	 * @return no.of days/weeks/months
	 */
	public static String getNoOfDays(Date givenDate) {

		if (null == givenDate)
			return "";

		Date today = new Date();
		DateTime start = new DateTime(givenDate);
		DateTime end = new DateTime(today);

		int noOfDays = Days.daysBetween(start.withTimeAtStartOfDay(),
				end.withTimeAtStartOfDay()).getDays();
		/*
		 * System.out.println("No.of days: "+noOfDays); String returnString =
		 * "";
		 * 
		 * if(noOfDays > 0){
		 * 
		 * if(noOfDays < 30){ returnString = String.valueOf(noOfDays)+"d ago";
		 * 
		 * }else if (noOfDays >= 30 && noOfDays < 90) { returnString =
		 * String.valueOf(noOfDays/7)+"w ago";
		 * 
		 * }else if (noOfDays >= 90) { returnString =
		 * String.valueOf(noOfDays/30)+"m ago";
		 * 
		 * }
		 * 
		 * }else if(noOfDays < 0){ noOfDays = Math.abs(noOfDays);
		 * 
		 * if(noOfDays < 30){ returnString = String.valueOf(noOfDays)+"d left";
		 * 
		 * }else if (noOfDays >= 30 && noOfDays < 90) { returnString =
		 * String.valueOf(noOfDays/7)+"w left";
		 * 
		 * }else if (noOfDays >= 90) { returnString =
		 * String.valueOf(noOfDays/30)+"m left";
		 * 
		 * }
		 * 
		 * }else{ return "(Today)";
		 * 
		 * }
		 * 
		 * return "("+returnString+")";
		 */
		if (noOfDays < 0 && noOfDays > -30) {
			return "(" + Math.abs(noOfDays) + "d left)";
		} else if (noOfDays > -90 && noOfDays <= -30) {
			return "(" + Math.abs(noOfDays) / 7 + "w left)";
		} else if (noOfDays <= -90) {
			return "(" + Math.abs(noOfDays) / 30 + "m left)";
		} else if (noOfDays == 0) {
			return "(Today)";
		} else if (noOfDays > 0 && noOfDays < 30) {
			return "(" + noOfDays + "d ago)";
		} else if (noOfDays >= 30 && noOfDays < 90) {
			return "(" + noOfDays / 7 + "w ago)";
		} else {
			return "(" + noOfDays / 30 + "m ago)";
		}
	}
	
	/**
	 * Used for tablet to show the time difference
	 * @author Liky_Pan
	 * @param givenDate
	 * @return A formatted string
	 */
	public static String getTimeDifferenceForPast(Date givenDate) {

		if (null == givenDate)
			return "";
		Date today = new Date();
		DateTime start = new DateTime(givenDate);
		DateTime end = new DateTime(today);

		int noOfMins = Minutes.minutesBetween(start,
				end).getMinutes();
		if (noOfMins == 0) {
			return "Now";
		}else if(noOfMins>0&&noOfMins<60){
			return noOfMins+" Min(s)";
		}else if(noOfMins>=60&&noOfMins<1440){
			return noOfMins/60+" Hour(s)";
		}else if(noOfMins>=1440&&noOfMins<43200){
			return noOfMins/1440+" Day(s)";
		}else if(noOfMins>=43200&&noOfMins<525600){
			return noOfMins/43200+" Month(s)";
		}else{
			return noOfMins/525600+ " Year(s)";
		}
	}
	
	/**
	 * format date to hours:mins
	 * @author Mars.su
	 * @param givenDate
	 * @return A formatted string
	 */
	public static String getTimeDifferenceHourAndMin(Date givenDate) {

		if (null == givenDate)
			return "";
		Date today = new Date();
		DateTime start = new DateTime(givenDate);
		DateTime end = new DateTime(today);
		int hours = 0;
		int mins = 0;
		int noOfMins = Minutes.minutesBetween(start,
				end).getMinutes();
		if (noOfMins == 0) {
			
		}else if(noOfMins>0&&noOfMins<60){
			mins = noOfMins;
		//}else if(noOfMins>=60&&noOfMins<1440){
		}else if(noOfMins>=60){
			hours =  noOfMins/60;
			mins = noOfMins%60;
		} 
		return hours+":"+mins;
	}
	
	
	
	/**
	 * Used for calculating the remaining time for my booking
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return A formatted string
	 */
	public static String calcRemainingTime(Date beginDate, Date endDate) {
		String remainingTime = "";
		if (beginDate == null || endDate == null) {
			return remainingTime;
		}
		Date todayDate = new Date();
		DateTime start = new DateTime(beginDate);
		DateTime end = new DateTime(endDate);
		DateTime today = new DateTime(todayDate);

		// noOfMinsStart>0, (start>today) for the Date Left Part; <=0,
		// (start<=today) for Ongoing
		int noOfMinsStart = Minutes.minutesBetween(today, start).getMinutes();
		// noOfMinsEnd>0, (today>end) for Complete; <=0, (today<=end) for
		// Ongoing
		int noOfMinsEnd = Minutes.minutesBetween(end, today).getMinutes();

		int hour = 60;//1h =60Minutes 1h
		int day = hour * 24;// 1440  1day
		int month = day * 30;// 43200 1moth
		int year = day * 365;// 525600  1year

		if (noOfMinsStart > 0) {
			if (noOfMinsStart > 0 && noOfMinsStart < hour) {
				remainingTime = noOfMinsStart + (noOfMinsStart == 1 ? " min" : " mins");
			} else if (noOfMinsStart >= hour && noOfMinsStart < day) {
				Integer hrCount = noOfMinsStart / hour;
				remainingTime = hrCount + (hrCount == 1 ? " hr" : " hrs");
			} else if (noOfMinsStart >= day && noOfMinsStart < month) {
				Integer dayCount = noOfMinsStart / day;
				if(noOfMinsStart % day > 0){
					remainingTime = dayCount +"days";
				}else{
					remainingTime = dayCount + "day" ;
				}
			} else if (noOfMinsStart >= month && noOfMinsStart < year) {
				Integer monthCount = noOfMinsStart / month;
				remainingTime = monthCount + (monthCount == 1 ? " mth" : " mths");
			} else {
				Integer yearCount = noOfMinsStart / year;
				remainingTime = yearCount + (yearCount == 1 ? " yr" : " yrs");
			}
			remainingTime = remainingTime + " left";
		}

		else if (noOfMinsStart <= 0 && noOfMinsEnd <= 0) {
			remainingTime = "Ongoing";
		}

		else if (noOfMinsEnd > 0) {
			remainingTime = "Complete";
		}
		return remainingTime;
	}
	
	public static String calcRemainingTime_new(Date beginDate, Date endDate) {
		String remainingTime = "";
		if (beginDate == null || endDate == null) {
			return remainingTime;
		}
		Date todayDate = new Date();
		DateTime start = new DateTime(beginDate);
		DateTime end = new DateTime(endDate);
		DateTime today = new DateTime(todayDate);

		// noOfMinsStart>0, (start>today) for the Date Left Part; <=0,
		// (start<=today) for Ongoing
		int noOfMinsStart = Minutes.minutesBetween(today, start).getMinutes();
		// noOfMinsEnd>0, (today>end) for Complete; <=0, (today<=end) for
		// Ongoing
		int noOfMinsEnd = Minutes.minutesBetween(end, today).getMinutes();
		//hour
		int hour = 60;//1h =60Minutes 1h
		//day
		int day = hour * 24;// 1440  1day
		//week( 30 to 90 days ) 
		int week=day*7;//week minutes
		int week_start=day*30;
		int week_end=day*90;
		
		//Months ( Over 90 days )
		int month = day * 30;// 1moth minutes
		int month_start = day * 90;// 43200 1moth
		//year
		int year = day * 365;// 525600  1year
		if (noOfMinsStart > 0) {
			//show mins
			if (noOfMinsStart > 0 && noOfMinsStart < hour) {
				remainingTime = noOfMinsStart + (noOfMinsStart == 1 ? " min" : " mins");
			}// show hours
			else if (noOfMinsStart >= hour && noOfMinsStart < day) {
				Integer hrCount = noOfMinsStart / hour;
				remainingTime = hrCount + (hrCount == 1 ? " hr" : " hrs");
			}//show day  less 30 day
			else if (noOfMinsStart >= day && noOfMinsStart < week_start) {
				Integer dayCount = noOfMinsStart / day;
				if(noOfMinsStart % day > 0){
					dayCount++;
				}
				remainingTime = dayCount + (dayCount == 1 ? " day" : " days");
			}//show week (30-90)
			else if (noOfMinsStart >= week_start && noOfMinsStart<week_end)
			{
				Integer weekCount = noOfMinsStart / week;//per week 7 day
				if(noOfMinsStart % week > 0){
					weekCount++;
				}
				remainingTime = weekCount + (weekCount == 1 ? " week" : " weeks ");
			}//show month
			 else if (noOfMinsStart >= week_end && noOfMinsStart < month_start) {
				Integer monthCount = noOfMinsStart / month;
				remainingTime = monthCount + (monthCount == 1 ? " month" : " months");
				
			}else if(noOfMinsStart>=month_start&&noOfMinsStart<year)
			{
				Integer monthCount = noOfMinsStart / month;
				remainingTime = monthCount + (monthCount == 1 ? " month " : " months ");
			} 
			 else {
				Integer yearCount = noOfMinsStart / year;
				remainingTime = yearCount + (yearCount == 1 ? " yr" : " yrs");
			}
			remainingTime = remainingTime + " left";
		}

		else if (noOfMinsStart <= 0 && noOfMinsEnd <= 0) {
			remainingTime = "Ongoing";
		}

		else if (noOfMinsEnd > 0) {
			remainingTime = "Complete";
		}
		return remainingTime;
	}
	private static int getDayBetweenDate(DateTime today,DateTime startDate)
	{
		 
		int day=Hours.hoursBetween(today, startDate).getHours()/24;
		return day;
		
	}
//	public static String calcRemainingTime_new(ResponseResult responseResult, Date beginDate, Date endDate) {
//		String remainingTime = "";
//		if (beginDate == null || endDate == null) {
//			return remainingTime;
//		}
//		Date todayDate = new Date();
//		DateTime start = new DateTime(beginDate);
//		DateTime end = new DateTime(endDate);
//		DateTime today = new DateTime(todayDate);
//
//		// noOfMinsStart>0, (start>today) for the Date Left Part; <=0,
//		// (start<=today) for Ongoing
//		int noOfMinsStart = Minutes.minutesBetween(today, start).getMinutes();
//		// noOfMinsEnd>0, (today>end) for Complete; <=0, (today<=end) for
//		// Ongoing
//		int noOfMinsEnd = Minutes.minutesBetween(end, today).getMinutes();
//		//hour
//		int hour = 60;//1h =60Minutes 1h
//		//day
//		int day = hour * 24;// 1440  1day
//		//week( 30 to 90 days ) 
//		int week=day*7;//week minutes
//		int week_start=day*30;
//		int week_end=day*90;
//		
//		//Months ( Over 90 days )
//		int month = day * 30;// 1moth minutes
//		int month_start = day * 90;// 43200 1moth
//		//year
//		int year = day * 365;// 525600  1year
//		if (noOfMinsStart > 0) {
//			//show mins
//			if (noOfMinsStart > 0 && noOfMinsStart < hour) {
//				remainingTime=responseResult.getI18nMessge(noOfMinsStart == 1?GTAError.BookingLangague.MIN.getCode():GTAError.BookingLangague.MINS.getCode(),new String[]{String.valueOf(noOfMinsStart)});
//			}// show hours
//			else if (noOfMinsStart >= hour && noOfMinsStart < day) {
//				Integer hrCount = noOfMinsStart / hour;
//				if(noOfMinsStart % hour > 0){
//					remainingTime=responseResult.getI18nMessge(GTAError.BookingLangague.HRS.getCode(),new String[]{String.valueOf(hrCount)});	
//				}else{
//					remainingTime=responseResult.getI18nMessge(GTAError.BookingLangague.HR.getCode(),new String[]{String.valueOf(hrCount)});
//				}
//				
//			}//show day  less 30 day
//			else if (noOfMinsStart >= day && noOfMinsStart < week_start) {
//				/***
//				 * SGG-3952
//				 * @time 2018-10-12
//				 * @desc display day update
//				 * @author christ
//				 */
//				DateTime bgTime = new DateTime(parseDate2String(beginDate, "yyyy-MM-dd"));
//				DateTime tTime = new DateTime(parseDate2String(todayDate, "yyyy-MM-dd"));
//				int dayCount=getDayBetweenDate(tTime, bgTime);
//				if(dayCount>1){
//					remainingTime=responseResult.getI18nMessge(GTAError.BookingLangague.DAYS.getCode(),new String[]{String.valueOf(dayCount)});
//				}else{
//					remainingTime=responseResult.getI18nMessge(GTAError.BookingLangague.DAY.getCode(),new String[]{String.valueOf(dayCount)});
//				}
////				Integer dayCount = noOfMinsStart / day;
////				if(noOfMinsStart % day > 0){
////				    remainingTime=responseResult.getI18nMessge(GTAError.BookingLangague.DAYS.getCode(),new String[]{String.valueOf(dayCount)});	
////				}else{
////					remainingTime=responseResult.getI18nMessge(GTAError.BookingLangague.DAY.getCode(),new String[]{String.valueOf(dayCount)});
////				}
//				
//			}//show week (30-90)
//			else if (noOfMinsStart >= week_start && noOfMinsStart<week_end)
//			{
//				Integer weekCount = noOfMinsStart / week;//per week 7 day
//				if(noOfMinsStart % week > 0){
//					weekCount++;
//				}
//				remainingTime=responseResult.getI18nMessge(weekCount == 1?GTAError.BookingLangague.WEEK.getCode():GTAError.BookingLangague.WEEKS.getCode(),new String[]{String.valueOf(weekCount)});
//			}//show month
//			 else if (noOfMinsStart >= week_end && noOfMinsStart < month_start) {
//				Integer monthCount = noOfMinsStart / month;
//				remainingTime=responseResult.getI18nMessge(monthCount == 1?GTAError.BookingLangague.MONTH.getCode():GTAError.BookingLangague.MONTHS.getCode(),new String[]{String.valueOf(monthCount)});
//				
//			}else if(noOfMinsStart>=month_start&&noOfMinsStart<year)
//			{
//				Integer monthCount = noOfMinsStart / month;
//				remainingTime = monthCount + (monthCount == 1 ? " month " : " months ");
//				remainingTime=responseResult.getI18nMessge(monthCount == 1?GTAError.BookingLangague.MONTH.getCode():GTAError.BookingLangague.MONTHS.getCode(),new String[]{String.valueOf(monthCount)});
//			} 
//			 else {
//				Integer yearCount = noOfMinsStart / year;
//				remainingTime=responseResult.getI18nMessge(yearCount == 1?GTAError.BookingLangague.YR.getCode():GTAError.BookingLangague.YRS.getCode(),new String[]{String.valueOf(yearCount)});
//			}
//		}
//		else if (noOfMinsStart <= 0 && noOfMinsEnd <= 0) {
//			remainingTime=responseResult.getI18nMessge(GTAError.BookingLangague.GOING.getCode());
//		}
//		else if (noOfMinsEnd > 0) {
//			remainingTime=responseResult.getI18nMessge(GTAError.BookingLangague.COMPLETE.getCode());
//		}
//		return remainingTime;
//	}
	/**
	 * 
	 * Used to count the activation days for enrollment
	 * @author Liky_Pan
	 * @param givenDate
	 */
	public static String getNoOfDaysInfuture(Date givenDate){
		if (null == givenDate)
			return "";

		DateTime start = new DateTime(new Date());
		DateTime end = new DateTime(givenDate);

		int noOfDays = Days.daysBetween(start.withTimeAtStartOfDay(),
				end.withTimeAtStartOfDay()).getDays();
		if(noOfDays==1){
			return "(" + Math.abs(noOfDays) + " day)";
		}
		else if(noOfDays>0){
			return "(" + Math.abs(noOfDays) + " days)";
		}else{
			return "";
		}
	}
	
	/**
	 * 
	 * Used to count the activation days for enrollment
	 * @author Liky_Pan
	 * @param givenDate
	 */
	public static Integer getNoOfDaysOnlyInfuture(Date givenDate){
		if (null == givenDate)
			return null;

		DateTime start = new DateTime(new Date());
		DateTime end = new DateTime(givenDate);

		Integer noOfDays = Days.daysBetween(start.withTimeAtStartOfDay(),
				end.withTimeAtStartOfDay()).getDays();
		if(noOfDays<0){
			return 0;
		}else{
			return noOfDays;	
		}
	}

	/**
	 * @author Allen_Yu
	 * @param givenDate
	 * @return ymdAndDateDiff String such as 2015/05/05(Today)
	 */
	public static String getYMDDateAndDateDiff(Date givenDate) {
		if (null == givenDate) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
		return df.format(givenDate) + " " + getNoOfDays(givenDate);
	}

	public static String getYMDDate(Date givenDate) {
		if (null == givenDate) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("yyyy-MMM-dd",Locale.ENGLISH);
		return df.format(givenDate);
	}

	public static String getFomattedDate(Date givenDate) {
		if (null == givenDate) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("ddMMMHHmmss");
		return df.format(givenDate);
	}

	/**
	 * @author Li_Chen
	 * @param givenDateStr
	 * @return getObliqueYMDDateAndDateDiff String such as 2015/04/22
	 *         17:01:55(16d ago)
	 */
	public static String getObliqueYMDDateAndDateDiff(Date givenDate) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return df.format(givenDate) + getNoOfDays(givenDate);
	}

	public static String getObliqueYMDDateAndDateDiff(Date givenDate,
			String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(givenDate) + getNoOfDays(givenDate);
	}

	public static Date parseString2Date(String src, String format) {

		if (StringUtils.isEmpty(src))
			return null;

		if (StringUtils.isEmpty(format))
			format = defaultformat;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = simpleDateFormat.parse(src);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static Date parseString2Date(String src, String format,Locale locale)throws ParseException {

		if (StringUtils.isEmpty(src))
			return null;

		if (StringUtils.isEmpty(format))
			format = defaultformat;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = null;
		date = simpleDateFormat.parse(src);
		return date;
	}
	

	/**
	 * @param givenDate
	 * @return ymdAndDateTimeDiff String such as 2015/05/05(Today)
	 * @author Vineela_Jyothi
	 */
	public static String getYMDDateTimeAndDateDiff(Date givenDate) {
		if (null == givenDate) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
		return df.format(givenDate) + " " + getNoOfDays(givenDate);
	}

	
   
   public static String parseDate2String(Date startDate,String format) {
	   DateFormat 	df = new SimpleDateFormat(format);
		String strDate = df.format(startDate);
		return strDate;
	}
   
       public static boolean isSameDay(Date day1, Date day2) {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
           String ds1 = sdf.format(day1);
           String ds2 = sdf.format(day2);
           if (ds1.equals(ds2)) {
               return true;
           } else {
               return false;
           }
       }
   
    /**
     * 判断时间是否是在指定时间内
     */
   	public static boolean isBetweenTime(Date date, Date[] scope) {
   		return (date.compareTo(scope[0]) >= 0 && date.compareTo(scope[1]) < 0);
   	}
   	
   	/** 给定日期前几天或者后几天的日期    */
    public static Date afterNowNDay(Date nowDate, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(nowDate);
        c.add(Calendar.DATE, n);
        return new Date(c.getTime().getTime());
    }
    /**
     * 比较时间大小
     * @param currentTime
     * @param moreTime
     * @return
     */
    public static boolean compareTime(Date currentTime,String moreTime) 
    {
        Date date1 ,date2;
        DateFormat formart = new SimpleDateFormat("HH:mm");
        try
        {
            date1 = formart.parse(formart.format(currentTime));
            date2 = formart.parse(moreTime);
            if(date1.compareTo(date2)<0)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch (ParseException e)
        {
            System.out.println("date init fail!");
            e.printStackTrace();
            return false;
        }
         
    }
    
	/** 给定日期前几天或者后几天的日期    */
    public static String afterNowNDayToString(Date nowDate, int n) {
    	DateFormat df = new SimpleDateFormat(defaultformatf);
		String strDate = df.format(afterNowNDay(nowDate, n));
		return strDate;
    }
    /**  设定一个日期时间，加几分钟（小时或者天）后得到新的日期 */
    public static Date addDateMinut(Date day, int x)//返回的是字符串型的时间，输入的
  //是String day, int x
   {   
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制  
  //引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变
  //量day格式一致
          Date date = day;   
//          try {   
//              date = format.parse(day);   
//          } catch (Exception ex) {   
//              ex.printStackTrace();   
//          }   
//          if (date == null)   
//              return "";   
          System.out.println("front:" + format.format(date)); //显示输入的日期  
          Calendar cal = Calendar.getInstance();   
          cal.setTime(date);   
          cal.add(Calendar.MINUTE, x);// 24小时制   
          date = cal.getTime();   
          System.out.println("after:" + format.format(date));  //显示更新后的日期 
          cal = null;   
//          return format.format(date);   
          return date;   
    
      }
	public static void main(String[] args) {
//		System.out.println(getStrFromDate(new Date()));
//		System.out.println(getDateFromStr("2013-01-22 17:01:55"));
//		System.out.println(getNoOfDays(getDateFromStr("2015-05-18 17:01:55")));
//		System.out.println(getObliqueYMDDateAndDateDiff(new Date()));
		
//		System.out.println(parseString2Date("2014/02/12", "yyyy/MM/dd"));
//		System.out.println(getDateFromStr("2013-01-22"));
//		System.out.println(getYMDDateAndDateDiff(getDateFromStr("2015-08-29 17:01:55")));
//		Date date = new Date();
//		System.out.println(date.getTime());
//		System.out.println(getFomattedDate(date));
//		Timestamp time = new Timestamp(new Date().getTime());
//		System.out.println(time);
//		System.out.println(parseDate2String(new Date(),"yyyy MMM dd"));
//		System.out.println(parseDate2String(new Date(),"MMM dd"));
//		
//		
//		System.out.println(DateConvertUtil.date2String(new Date(), "yyyy-MM-dd"));
//		String str = DateConvertUtil.date2String(new Date(), "yyyy-MM-dd") + " 23:59:59";
//		System.out.println(str);
//		
//		//System.out.println(DateConvertUtil.df);
//		System.out.println(getDateFromStr(str));
//		
//		
//		System.out.println(getDateFromStr(DateConvertUtil.date2String(new Date(), "yyyy-MM-dd")+" 23:59:59"));
//		
//		System.out.println(getTimeDifferenceForPast(parseString2Date("2011/11/13 10:59", "yyyy/MM/dd HH:mm")));
//		
//		System.out.println("------>" + afterNowNDayToString(new Date(), 14));
//		System.out.println(getTimeDifferenceHourAndMin(new Date()));
//		addDateMinut(new Date(), 5);
//		DateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMdd");
//		String currentDate = fileNameDateFormat.format(d);
//		System.out.println(currentDate);
		
	}


	public static String formatCurrentDate(Date d) {
		DateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMdd");
		String currentDate = fileNameDateFormat.format(d);
		return currentDate;
	}
	public static String formatDate(Date d, String format) {
		DateFormat fileNameDateFormat = new SimpleDateFormat(format);
		String currentDate = fileNameDateFormat.format(d);
		return currentDate;
	}
	
	public static String formatCurrentDate(Date d,String DateFormat) {
		DateFormat fileNameDateFormat = new SimpleDateFormat(DateFormat);
		String currentDate = fileNameDateFormat.format(d);
		return currentDate;
	}
	public static Date parseCurrentDate(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date currentDate;
		try {
			currentDate = sdf.parse(dateStr);
			return currentDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public static Date parseCurrentDateWithTime(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date currentDate;
		try {
			currentDate = sdf.parse(dateStr);
			return currentDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public static boolean isWithinRange(Date testDate,Date startDate,Date endDate) {
	    return testDate.getTime() >= startDate.getTime() &&
	             testDate.getTime() <= endDate.getTime();
	}
	 
}
