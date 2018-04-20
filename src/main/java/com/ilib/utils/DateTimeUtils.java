package com.ilib.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日期时间处理工具类
 *
 * @auto zhangqing
 * @date 2013-12-10
 */
public class DateTimeUtils {

	/**
	 * 获得指定格式的当前日期。
	 *
	 * @param format "25NOV13"
	 * @return "2013-11-25"
	 */
	public static final String getDateByShopping(String date) {
		String day = date.substring(0, 2);
		String mon = date.substring(2, 5);
		String year = date.substring(5, 7);
		if (mon.equals("JAN")) {
			date = "20" + year + "-01" + "-" + day;
		} else if (mon.equals("FEB")) {
			date = "20" + year + "-02" + "-" + day;
		} else if (mon.equals("MAR")) {
			date = "20" + year + "-03" + "-" + day;
		} else if (mon.equals("APR")) {
			date = "20" + year + "-04" + "-" + day;
		} else if (mon.equals("MAY")) {
			date = "20" + year + "-05" + "-" + day;
		} else if (mon.equals("JUN")) {
			date = "20" + year + "-06" + "-" + day;
		} else if (mon.equals("JUL")) {
			date = "20" + year + "-07" + "-" + day;
		} else if (mon.equals("AUG")) {
			date = "20" + year + "-08" + "-" + day;
		} else if (mon.equals("SEP")) {
			date = "20" + year + "-09" + "-" + day;
		} else if (mon.equals("OCT")) {
			date = "20" + year + "-10" + "-" + day;
		} else if (mon.equals("NOV")) {
			date = "20" + year + "-11" + "-" + day;
		} else if (mon.equals("DEC")) {
			date = "20" + year + "-12" + "-" + day;
		}
		return date;
	}

	/**
	 * 将java.util.Date类型日期转化为“yyyy-MM-dd”格式的String类型日期
	 *
	 * @param date java.util.Date类型日期
	 * @return String类型日期
	 */
	public static String getDate(java.util.Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 返回字符型时间("HH:mm:ss")
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回字符型时间
	 */
	public static String getTime(java.util.Date date) {
		return formatDate(date, "HH:mm:ss");
	}

	/**
	 * 返回字符型时间("HH:mm")
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回字符型时间
	 */
	public static String getTimeHHMM(java.util.Date date) {
		return formatDate(date, "HH:mm");
	}

	/**
	 * 返回字符型日期时间("yyyy-MM-dd HH:mm:ss")
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回字符型日期时间
	 */
	public static String getDateTime(java.util.Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将java.util.Date类型日期转化为指定格式的String类型日期
	 *
	 * @param date java.util.Date类型日期
	 * @param format 指定的日期格式
	 * @return String类型日期
	 */
	public static String formatDate(java.util.Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 将String类型日期转化为指定格式的java.util.Date类型日期
	 *
	 * @param dateStr String类型日期
	 * @param format 格式
	 * @return java.util.Date类型日期
	 */
	public static java.util.Date parseDate(String dateStr, String format) {
		java.util.Date date = null;
		if (dateStr.length() < format.length()) {
			format = format.substring(0, dateStr.length());
		}
		try {
			java.text.DateFormat df = new SimpleDateFormat(format);
			date = (java.util.Date) df.parse(dateStr);
		} catch (Exception e) {
			// System.out.println("日期解析错误:" + dateStr);
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将String类型日期转化为“yyyy-MM-dd”格式的java.util.Date类型日期
	 *
	 * @param dateStr String类型日期
	 * @return java.util.Date类型日期
	 */
	public static java.util.Date parseDate(String dateStr) {
		return parseDate(dateStr, "yyyy-MM-dd");
	}

	/**
	 * 将java.sql.Date类型日期转化为java.util.Date类型日期
	 *
	 * @param date java.sql.Date类型日期
	 * @return java.util.Date类型日期
	 */
	public static java.util.Date parseDate(java.sql.Date date) {
		return date;
	}

	/**
	 * 将java.util.Date类型日期转化为java.sql.Date类型日期
	 *
	 * @param date java.util.Date类型日期
	 * @return java.sql.Date类型日期
	 */
	public static java.sql.Date parseSqlDate(java.util.Date date) {
		if (date != null)
			return new java.sql.Date(date.getTime());
		else
			return null;
	}

	/**
	 * 将String类型日期转化为指定格式的java.sql.Date类型日期
	 *
	 * @param dateStr String类型日期
	 * @param format 指定格式
	 * @return java.sql.Date类型日期
	 */
	public static java.sql.Date parseSqlDate(String dateStr, String format) {
		java.util.Date date = parseDate(dateStr, format);
		return parseSqlDate(date);
	}

	/**
	 * 将String类型日期转化为"yyyy-MM-dd"格式的java.sql.Date类型日期
	 *
	 * @param dateStr String类型日期
	 * @return java.sql.Date类型日期
	 */
	public static java.sql.Date parseSqlDate(String dateStr) {
		return parseSqlDate(dateStr, "yyyy-MM-dd");
	}

	/**
	 * 将String类型日期转化为指定格式的java.sql.Timestamp类型日期
	 *
	 * @param dateStr String类型日期
	 * @param format 指定格式
	 * @return java.sql.Timestamp类型日期
	 */
	public static java.sql.Timestamp parseTimestamp(String dateStr, String format) {
		java.util.Date date = parseDate(dateStr, format);
		if (date != null) {
			long t = date.getTime();
			return new java.sql.Timestamp(t);
		} else
			return null;
	}

	/**
	 * 将String类型日期转化为"yyyy-MM-dd HH:mm:ss"格式的java.sql.Timestamp类型日期
	 *
	 * @param dateStr String类型日期
	 * @return java.sql.Timestamp类型日期
	 */
	public static java.sql.Timestamp parseTimestamp(String dateStr) {
		return parseTimestamp(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 返回年份
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回年份
	 */
	public static int getYear(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.YEAR);
	}

	/**
	 * 返回月份
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回月份
	 */
	public static int getMonth(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.MONTH) + 1;
	}

	/**
	 * 返回日
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回日份
	 */
	public static int getDay(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回小时
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回小时
	 */
	public static int getHour(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.HOUR_OF_DAY);
	}

	/**
	 * 返回分钟
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回分钟
	 */
	public static int getMinute(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.MINUTE);
	}

	/**
	 * 返回秒钟
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回秒钟
	 */
	public static int getSecond(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.SECOND);
	}

	/**
	 * 返回毫秒
	 *
	 * @param date java.util.Date类型日期
	 * @return 返回毫秒
	 */
	public static long getMillis(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 日期相加
	 *
	 * @param date java.util.Date类型日期
	 * @param day 天数
	 * @return 返回相加后的日期
	 */
	public static java.util.Date addDate(java.util.Date date, int day) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 日期相减
	 *
	 * @param date java.util.Date类型日期
	 * @param date1 日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(java.util.Date date, java.util.Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 日期字符串（yyyy-MM-dd HH:mm格式）- 当前时间 的分钟数
	 *
	 * @param str 日期字符串（yyyy-MM-dd HH:mm格式）
	 * @return 返回相减后的分钟数
	 */
	public static int diffCurrentDate(String str) {
		java.util.Date dt = parseDate(str, "yyyy-MM-dd HH:mm");
		return (int) ((getMillis(dt) - getMillis(new java.util.Date())) / (60 * 1000));
	}

	/**
	 * 获得小时差(参数字符串格式为：yyyy-MM-dd HH:mm:ss)
	 *
	 * @param d1 日期1
	 * @param d2 日期2
	 * @return d2-d1的小时差
	 */
	public static String diffHour(String d1, String d2) {
		return String.valueOf(
				(parseDate(d2, "yyyy-MM-dd HH:mm:ss").getTime() - parseDate(d1, "yyyy-MM-dd HH:mm:ss").getTime())
						/ (1000 * 3600));
	}

	/**
	 * 日期相减
	 *
	 * @param date java.util.Date类型日期
	 * @param date1 日期
	 * @return 返回相减后的分钟
	 */
	public static int diffMin(java.util.Date date, java.util.Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (1000 * 60));
	}

	/**
	 * 日期相减
	 *
	 * @param date java.util.Date类型日期
	 * @param date1 日期
	 * @return 返回相减后的毫秒数
	 */
	public static int diff(java.util.Date date, java.util.Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)));
	}

	/**
	 * 日期相减
	 *
	 * @param date java.util.Date类型日期
	 * @param date1 日期
	 * @return 返回相减后的毫秒数
	 */
	public static long diffMillis(java.util.Date date, java.util.Date date1) {
		return ((getMillis(date) - getMillis(date1)));
	}

	/**
	 * 判断一个字符串是否是合法的yyyy-MM-dd格式
	 *
	 * @param strDate
	 * @return true合法 false不合法
	 */
	public static boolean isShortDate(String strDate) {
		String reg = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
		return strDate.matches(reg);
	}

	/**
	 * 根据长日期（yyyy-MM-dd HH:mm:ss格式）获取短日期（yyyy-MM-dd格式）
	 *
	 * @param date 长日期（yyyy-MM-dd HH:mm:ss格式）
	 * @return 短日期（yyyy-MM-dd格式）
	 */
	public static java.util.Date getShortDate(java.util.Date date) {
		return parseDate(getDate(date), "yyyy-MM-dd");
	}

	/**
	 * 日期字符串（yyyy-MM-dd HH:mm格式）+ X分钟 后的日期字符串
	 *
	 * @param str 日期字符串（yyyy-MM-dd HH:mm格式）
	 * @param minute 分钟
	 * @return 日期字符串（yyyy-MM-dd HH:mm格式）
	 */
	public static String getDateAddMinute(String str, int minute) {
		java.util.Date dt = parseDate(str, "yyyy-MM-dd HH:mm");
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTimeInMillis(getMillis(dt) + minute * 60 * 1000);
		return formatDate(c.getTime(), "yyyy-MM-dd HH:mm");
	}

	/**
	 * 判断当前时间是否在指定的时间范围内
	 *
	 * @param startTime 起始时间 （ HH:mm格式）
	 * @param endTime 结束时间（ HH:mm格式）
	 * @return true 是 false 否
	 */
	public static boolean compareDateRange(String startTime, String endTime) throws Exception {
		// 当前时间
		java.util.Date currDate = new java.util.Date();
		String currDateStr = DateTimeUtils.getDate(currDate);

		// 开始时间
		java.util.Date sDate = parseDate(currDateStr + " " + startTime, "yyyy-MM-dd HH:mm");
		Calendar scal = Calendar.getInstance();
		scal.setTime(sDate);
		// 0秒
		scal.set(Calendar.SECOND, 0);
		sDate = scal.getTime();

		// 结束时间
		java.util.Date eDate = parseDate(currDateStr + " " + endTime, "yyyy-MM-dd HH:mm");
		Calendar ecal = Calendar.getInstance();
		ecal.setTime(eDate);
		// 59秒
		ecal.set(Calendar.SECOND, 59);
		eDate = ecal.getTime();

		// 意为currDate >=sDate and currDate<=eDate
		if (currDate.compareTo(sDate) >= 0 && currDate.compareTo(eDate) <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 给定时间增加hour小时数
	 *
	 * @Description:
	 * @author weisd
	 * @date 2014年6月3日 上午10:03:54
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getDateByAddByHour(Date date, int hour) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(c.HOUR, hour);
			return c.getTime();
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 给定时间增加
	 *
	 * @Description:
	 * @author weisd
	 * @date 2014年6月3日 上午10:03:54
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getDateByAddByYear(Date date, int year) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.YEAR, year);
			return c.getTime();
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 给定时间增加
	 *
	 * @Description:
	 * @author weisd
	 * @date 2014年6月3日 上午10:03:54
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getDateByAddByMin(Date date, int min) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MINUTE, min);
			return c.getTime();
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 给定时间增加
	 *
	 * @Description:
	 * @author weisd
	 * @date 2014年6月3日 上午10:03:54
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getDateByAddBySecond(Date date, int second) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.SECOND, second);
			return c.getTime();
		} catch (Exception e) {

		}
		return null;
	}

	public static Date getDateByDay(Date date, int day) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, day);
			return c.getTime();
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 与给定的日期做分钟运算
	 *
	 * @date 2014年6月20日
	 * @param date 字符串日期(格式：yyyy-MM-dd HH:mm:ss)
	 * @param min 分钟
	 * @return
	 */
	public static Date getDateByMinute(String date, int min) {
		try {
			Date d = parseDate(date, "yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.MINUTE, min);
			return c.getTime();
		} catch (Exception e) {

		}
		return null;
	}

	public static void main(String[] arg) throws Exception {
		// //System.out.println(diffCurrentDate("2014-11-19 23:38:10"));
		// String dateStr = "2015-03-01 00:38:10";
		// String format = "yyyy-MM-dd HH:mm:ss";
		// Date date = DateTimeUtils.parseDate(dateStr, format);
		// int hour = -2;
		// Date dateN = DateTimeUtils.getDateByAddByHour(date, hour);
		// //System.out.println(hour);
		// //System.out.println(dateStr);
		// //System.out.println(DateTimeUtils.formatDate(dateN, format));

		// int hour = -2;
		// int hour = -1;
		// String date_str = DateTimeUtils.formatDate(new Date(), "yyyy-MM-dd");
		// Date date = DateTimeUtils.parseDate(date_str, "yyyy-MM-dd");
		// Date date_pre = DateTimeUtils.getDateByAddByHour(date, hour);
		// String date_pre_str = DateTimeUtils.formatDate(date_pre,
		// "yyyy-MM-dd HH:mm:ss");
		// //System.out.println(date_pre_str);
		// //System.out.println(date_str);

		// String old = "2010-12-28";
		// String old = "2002-12-13";
		// Date date = DateTimeUtils.parseDate(old, "yyyy-MM-dd");
		// int year = 12;
		// Date date_after_12 = DateTimeUtils.getDateByAddByYear(date, year);
		// String date_pre_str = DateTimeUtils.formatDate(date_after_12,
		// "yyyy-MM-dd");
		// //System.out.println(old);
		// //System.out.println(date_pre_str);
		//
		// int ddd = DateTimeUtils.diffDate(new Date(), date_after_12);
		//
		// //System.out.println(ddd);
		// String outDate = "2014-12-13";
		// Date date = DateTimeUtils.parseDate("2014-12-12", "yyyy-MM-dd");
		// Date currentDate = DateTimeUtils.parseDate(outDate, "yyyy-MM-dd");
		// int dif = currentDate.compareTo(date);
		//
		// //System.out.println(dif);
		// if(dif <= 0){
		// //System.out.println("ok");
		// }

		// Q.isChildren(cardno, takeoffDate);
		// String time = "2014-12-12 23:20:30";
		String time = "2014-12-12 00:02:20";
		// System.out.println(time);
		// Date date = DateTimeUtils.parseDate(time, "yyyy-MM-dd HH:mm:ss");
		// Date dateNew = DateTimeUtils.getDateByAddByMin(date, -1);
		// String dd = DateTimeUtils.formatDate(dateNew, "yyyy-MM-dd HH:mm:ss");
		// //System.out.println(dd);
		// Date datebefore = DateTimeUtils.getDateByAddByHour(date, -2);// 2小时以前
		// String dd2 = DateTimeUtils.formatDate(datebefore,
		// "yyyy-MM-dd HH:mm:ss");
		// //System.out.println(dd2);

		Date date = DateTimeUtils.parseDate(time, "yyyy-MM-dd HH:mm:ss");
		Date dateNew = DateTimeUtils.getDateByAddBySecond(date, -70);
		String dd = DateTimeUtils.formatDate(dateNew, "yyyy-MM-dd HH:mm:ss");
		// System.out.println(dd);
	}

	/**
	 * @describe datetime类型转换成date类型
	 * @auto zwliao
	 * @date 2014-2-11 上午11:04:48
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date transDate(Date date) throws Exception {
		SimpleDateFormat simf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = simf.format(date);
		return simf.parse(dateString);
	}

	/**
	 * @describe 在当前日期上增加或者减去指定的天数
	 * @auto zwliao
	 * @date 2014-10-15 上午9:53:01
	 * @param dayNum 需要增加或减去的天数
	 * @return
	 */
	public static String addOrReduceDateBaseCurrentDate(int dayNum) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());   				// 设置当前日期
		c.add(Calendar.DAY_OF_MONTH, dayNum);   // 日期变动天数
		Date date = c.getTime(); 				// 结果
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * @describe 格式化日期的格式
	 * @author xiangcong
	 * @date 2015-4-27上午11:54:31
	 * @param dateStr date的string格式 例如:Wed May 20 00:00:00 CST 2015
	 * @return
	 */
	public static String formatDateStr(String dateStr, String format) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
		try {
			Date date = sdf1.parse(dateStr);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			String sDate = sdf.format(date);
			return sDate;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 得到一年的天数
	 * 
	 * @return
	 */
	public static int getYearDays() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getActualMaximum(Calendar.DAY_OF_YEAR);// 一年的天数
	}

	/**
	 * 获取当年的第一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date getCurrYearFirst() {
		Calendar currCal = Calendar.getInstance();
		currCal.set(Calendar.DAY_OF_YEAR, 1);
		return currCal.getTime();
	}

	/**
	 * 判断是否周末
	 * 
	 * @return
	 */
	public static boolean isWeekEnd(Calendar cal) {
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return true;
		}
		return false;

	}

	/**
	 * 得到一个月的所有日期
	 * 
	 * @param date
	 * @return
	 */
	public static List<String> getAllTheDateOftheMonth(Date date) {
		List<String> list = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);

		int month = cal.get(Calendar.MONTH);
		while (cal.get(Calendar.MONTH) == month) {
			list.add(DateTimeUtils.getDate(cal.getTime()));
			cal.add(Calendar.DATE, 1);
		}
		return list;
	}

	/**
	 * 一个月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		return DateTimeUtils.getDate(cal.getTime());
	}

	public static Date getDateAddDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, c.get(Calendar.DATE) + day);
		return c.getTime();
	}

	public static Date getNowDate() {
		Date date = new Date();
		String datestr = formatDate(date, "yyyy-MM-dd");
		return parseDate(datestr, "yyyy-MM-dd");
	}

	/**
	 * 一个月最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.DATE, -1);
		return DateTimeUtils.getDate(cal.getTime());
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate 较小的时间
	 * @param bdate 较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static List<Date> dateSplit(Date startDate, Date endDate) throws Exception {
		if (!startDate.before(endDate))
			throw new Exception("开始时间应该在结束时间之后");
		Long spi = endDate.getTime() - startDate.getTime();
		Long step = spi / (24 * 60 * 60 * 1000);// 相隔天数
		List<Date> dateList = new ArrayList<Date>();
		dateList.add(endDate);
		for (int i = 1; i <= step; i++) {
			dateList.add(new Date(dateList.get(i - 1).getTime() - (24 * 60 * 60 * 1000)));// 比上一天减一
		}
		return dateList;
	}

	public static final String getEngDate(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int mon = c.get(Calendar.MONTH);
		int da = c.get(Calendar.DAY_OF_MONTH);
		String day = da + "";
		// String year = (c.get(Calendar.YEAR) + "").substring(2, 4);
		if (da < 10) {
			day = "0" + da;
		}
		switch (mon) {
		case 0:
			return day + "JAN";
		case 1:
			return day + "FEB";
		case 2:
			return day + "MAR";
		case 3:
			return day + "APR";
		case 4:
			return day + "MAY";
		case 5:
			return day + "JUN";
		case 6:
			return day + "JUL";
		case 7:
			return day + "AUG";
		case 8:
			return day + "SEP";
		case 9:
			return day + "OCT";
		case 10:
			return day + "NOV";
		case 11:
			return day + "DEC";
		default:
		}
		return null;
	}

	/**
	 * 比较日期
	 *
	 * @param startdate
	 * @param endDate
	 * @param otherdate
	 * @return 返回1，表示otherdate不在startdate和endDate之间
	 */
	public static String compareDate(Date startdate, Date endDate, Date otherdate) {
		String ret = "-1";
		if (startdate == null && endDate == null) {

		} else if (startdate == null && endDate != null) {
			int count2 = DateTimeUtils.diffDate(otherdate, endDate);
			if (count2 > 0) {// 表示otherdate大于enddate
				ret = "1";
			}
		} else if (startdate != null && endDate == null) {
			int count1 = DateTimeUtils.diffDate(otherdate, startdate);
			if (count1 < 0) {// 表示otherdate 小于 startdate
				ret = "1";
			}
		} else if (startdate != null && endDate != null) {
			int count1 = DateTimeUtils.diffDate(otherdate, startdate);
			int count2 = DateTimeUtils.diffDate(otherdate, endDate);
			if (count1 < 0 || count2 > 0) {// otherdate小于startdate,或大于endDate
				ret = "1";
			}
		}
		return ret;
	}
	/**
	 * 1945转换成19:45 
	 * add by Kyoxue
	 */
	public static String formatTime(String time){
		if (null==time||time.trim().equals("")) {
			return "";
		}
		if (time.contains(":")) {
			return time;
		}
		if (time.length()!=4) {
			return "";
		}
		char[] chars = time.toCharArray();
		char[] newChars = new char[5];
		char first = chars[0];//1
		char second =chars[1];//9
		char third =chars[2];//4
		char fourth =chars[3];//5
		newChars[0]=first;
		newChars[1]=second;
		newChars[2]=':';
		newChars[3]=third;
		newChars[4]=fourth;
		return new String(newChars);
	}
	/**
	 * 计算抵达日期
	 * add by Kyoxue
	 * 起飞时间小于抵达时间，起飞日期是抵达日期；起飞时间大于抵达时间，起飞时间+1天
	 */
	public static String calArrivalDate(String date,String depTime,String arrTime){
		if (StringUtil.get().isNull(date)||StringUtil.get().isNull(depTime)||StringUtil.get().isNull(arrTime)) {
			return "";
		}
		try {
			Date depDate = parseDate(date);
			int depT = Integer.valueOf(depTime);
			int arrT = Integer.valueOf(arrTime);
			if (depT<=arrT) {
				return date;
			}else{
				Date arrDate = addDate(depDate, 1);
				return getDate(arrDate);
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获取毫秒数对应的日期时间
	 */
	public static String getDateTimeFromMill(long mill){
		String dateStr = "";
		try {
			Date date = new Date(mill);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateStr = sdf.format(date);
		} catch (Exception e) {
		}
		return dateStr;
	}
	
	/**
	 * 计算毫秒数之间的秒数
	 */
	public static String getMillDiff(long start,long end){
		long diffMill  = end-start;
		double diffSec = diffMill*1.0/1000;
		return String.format("%.2f", diffSec);
	}
	/**
	 * 获取毫秒数对应的日期时间 yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static String getDateTimeFullFromMill(long mill){
		String dateStr = "";
		try {
			Date date = new Date(mill);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			dateStr = sdf.format(date);
		} catch (Exception e) {
		}
		return dateStr;
	}
	/**
	 * 计算毫秒数之间的秒数 精确到小数2位四舍五入 返回BigDecimal
	 * @param start
	 * @param end
	 * @return
	 */
	public static BigDecimal getMillDiffDecimal(long start,long end){
		BigDecimal bdStart = new BigDecimal(start);
		BigDecimal bdEnd = new BigDecimal(end);
		BigDecimal bdDiff = bdEnd.subtract(bdStart);
		BigDecimal bdOneThousand = new BigDecimal(1000);
		BigDecimal bdResult = bdDiff.divide(bdOneThousand).setScale(2, RoundingMode.HALF_UP);
		return bdResult;
	}
}
