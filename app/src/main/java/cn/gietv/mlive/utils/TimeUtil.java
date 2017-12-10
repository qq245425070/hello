package cn.gietv.mlive.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author houde
 * 
 */
public class TimeUtil {

	private static final long ONE_MINUTE = 60000L;
	private static final long ONE_HOUR = 3600000L;
	private static final String ONE_SECOND_AGO = "秒前";
	private static final String ONE_MINUTE_AGO = "分钟前";
	private static final String ONE_HOUR_AGO = "小时前";

	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}
	public static String getYearAndMonth(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
		return format.format(getDate(date));
	}
	public static String getDay(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(getDate(date));
	}

	public static String getHourAndMonth(String date) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
		return format.format(getDate(date));
	}

	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;

		default:
			// result = temp + "天前 ";
			result = getTime(timesamp);
			break;
		}

		return result;
	}
	public static Date getDate(String dateString){
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = sdf.parse(dateString);
			return date;
		}
		catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}
		return new Date();
	}
	public static String getTimeString(long t){
		long time = t - System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		long days = time / 24 / 60 / 60 / 1000;
		long hours = time % ( 24 * 60 *60 * 1000) / 3600 / 1000;
		long minutes = time % (60 * 60 * 1000 ) / 60 / 1000;
		long seconds = time % ( 60 * 1000 ) / 1000;
		if(days > 0){
			System.out.println(" days" + days);
			sb.append(days + "天");
		}
		if(hours > 0){
			System.out.println( " hours" + hours);
			sb.append(hours + "小时");
		}
		if(minutes > 0){
			System.out.println( " minutes" + minutes);
			sb.append(minutes + "分");
		}
		if(seconds > 0){
			System.out.println( " seconds" + seconds);
			sb.append(seconds + "秒");
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	public static String getTimeStringMedia(long t){
		long time = t - System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		long days = time / 24 / 60 / 60 / 1000;
		long hours = time % ( 24 * 60 *60 * 1000) / 3600 / 1000;
		long minutes = time % (60 * 60 * 1000 ) / 60 / 1000;
		long seconds = time % ( 60 * 1000 ) / 1000;
		if(days > 0){
			System.out.println(" days" + days);
			sb.append(days + ":");
		}
		if(hours > 0){
			System.out.println( " hours" + hours);
			sb.append(hours + ":");
		}
		if(minutes > 0){
			System.out.println( " minutes" + minutes);
			sb.append(minutes + ":");
		}
		if(seconds > 0){
			System.out.println( " seconds" + seconds);
			sb.append(seconds + ":");
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	public static String format(String dateString) {
		Date date = getDate(dateString);
		long delta = new Date().getTime() - date.getTime();
		if (delta < 1L * ONE_MINUTE) {
			long seconds = toSeconds(delta);
			return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
		}
		if (delta < 45L * ONE_MINUTE) {
			long minutes = toMinutes(delta);
			return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
		}
		if (delta < 24L * ONE_HOUR) {
			long hours = toHours(delta);
			return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
		}
		if (delta < 48L * ONE_HOUR) {
			return "昨天";
		}
		return getDay(dateString);
	}

	private static long toSeconds(long date) {
		return date / 1000L;
	}

	private static long toMinutes(long date) {
		return toSeconds(date) / 60L;
	}

	private static long toHours(long date) {
		return toMinutes(date) / 60L;
	}
}
