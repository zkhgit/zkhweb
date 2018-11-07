
package zkh.tool.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

import zkh.tool.date.common.Format;

import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
 * 时间格式化工具
 *
 * 赵凯浩
 * 2017年9月11日 上午11:23:49
 */
public class DateUtil {
	
	/**
	 * 日期转字符串
	 * 默认：yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @param method
	 * @return
	 */
	public static String dateToStr(Date date, String method) {
		if(date == null) return null;
		method = StringUtils.isBlank(method)?Format.COMPLETE:method;
		DateFormat df = new SimpleDateFormat(method);
    	return df.format(date); 
	}
	
	/**
	 * 字符串转日期 
	 * @param dateStr
	 * @param method
	 * @return
	 */
	public static Date strToDate(String dateStr, String method) { 
		Date date = null; 
		method = StringUtils.isBlank(method)?Format.COMPLETE:method;
	    SimpleDateFormat format = new SimpleDateFormat(method); 
	    try { 
	    	date = format.parse(dateStr); 
	    } catch (Exception e) { 
	    	e.printStackTrace(); 
	    }
	    return date; 
	} 
    
	/**
	 * 得到指定日期
	 * 描述：将天、小时、分钟向前或向后推移（可以推移超过24）
	 * @param type DATE HOUR MINUTE
	 * @param num ...-2 -1 0 1 2 ...
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getAppointDate(String type, int num){
		if (StringUtils.isBlank(type)) return null;
		Date date=new Date();
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(date); 
		int type_ = type.equals("DATE") ? calendar.DATE : type.equals("HOUR") ? calendar.HOUR : calendar.MINUTE;
		calendar.add(type_,-1); //把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 得到指定日期的字符串
	 * 描述：将天、小时、分钟向前或向后推移（可以推移超过24）
	 * @param type DATE HOUR MINUTE
	 * @param num ...-2 -1 0 1 2 ...
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getAppointDateString(String type, int num){
		if(StringUtils.isBlank(type)) return null;
		Date date = new Date();
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(date); 
		int type_ = type.equals("DATE") ? calendar.DATE : type.equals("HOUR") ? calendar.HOUR : calendar.MINUTE;
		calendar.add(type_,num); //把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(Format.COMPLETE);
		String dateString = formatter.format(date);
		return dateString;
	}
	
}