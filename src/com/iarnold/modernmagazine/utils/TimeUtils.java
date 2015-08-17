package com.iarnold.modernmagazine.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.net.ParseException;
import android.text.format.DateFormat;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat DATE_FORMAT_DATE2 = new SimpleDateFormat(
			"yyyy-M-d");
	public static final SimpleDateFormat DATE_FORMAT_DATE3 = new SimpleDateFormat(
			"yyyy.M.d");

	public static Date getDateByStr(String dd) {

		Date date;
		try {
			date = DATE_FORMAT_DATE.parse(dd);
		} catch (java.text.ParseException e) {
			date = null;
			e.printStackTrace();
		}
		return date;
	}

	//
	// /**
	// * 将现在时间转换成 yyyy-MM-dd
	// *
	// * @return 现在时间的yyyy-MM-dd
	// */
	// public static String getDate() {
	// return getTime(System.currentTimeMillis(), DATE_FORMAT_DATE);
	// }

	/**
	 * 将现在时间转换成 yyyy.M.d
	 * 
	 * @return 现在时间的yyyy.M.d
	 */
	public static String getDate() {
		return getTime(System.currentTimeMillis(), DATE_FORMAT_DATE3);
	}

	/**
	 * 将Date 转换成 yyyy-mm-dd输出
	 * 
	 * @param date
	 * @return
	 */
	public static String getDate(Date date) {
		return DATE_FORMAT_DATE.format(date);
	}

	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

}