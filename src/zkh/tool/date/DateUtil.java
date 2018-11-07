
package zkh.tool.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * 时间格式化工具
 *
 * 赵凯浩
 * 2017年9月11日 上午11:23:49
 */
public class DateUtil {
	
	/** 完整时间 yyyy-MM-dd HH:mm:ss */
	private static final String simple  = "yyyy-MM-dd HH:mm:ss";

	/** 年月日时分秒(无下划线) yyyyMMddHHmmss */
	private static final String dtLong  = "yyyyMMddHHmmss";
    
    /** 年月日(无下划线) yyyyMMdd */
	private static final String dtShort = "yyyyMMdd";
	
    /**
     * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
     * 
     * @return
     */
    public  static String getSimpleTime(){
    	Date date=new Date();
    	DateFormat df=new SimpleDateFormat(simple);
    	return df.format(date);
    }
    
    public  static String getSimpleTime(Date date){
    	DateFormat df=new SimpleDateFormat(simple);
    	return df.format(date);
    }
    
    /**
     * 返回系统当前时间(精确到毫秒),格式：yyyyMMddHHmmss
     * 
     * @return
     */
	public  static String getLongTime(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtLong);
		return df.format(date);
	}
	
	/**
	 * 自定义
	 * 
	 * @return
	 */
	public static String userDefined(String myIdea){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(myIdea);
		return df.format(date);
	}
	
	/**
	 * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
	 * @return
	 */
	public static String getShortTime(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtShort);
		return df.format(date);
	}
	
	/**
	 * 得到指定日期
	 * @param type DATE HOUR MINUTE
	 * @param num ...-2 -1 0 1 2 ...
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getAppointDate(String type, int num){
		if (type==null || type.equals("")) {
			return null;
		}
		Date date=new Date();
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(date); 
		int type_ = type.equals("DATE")?calendar.DATE:type.equals("HOUR")?calendar.HOUR:calendar.MINUTE;
		calendar.add(type_,-1); //把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime();
		return date;
	}
	
	/**
	 * 得到指定日期的字符串
	 * @param type DATE HOUR MINUTE
	 * @param num ...-2 -1 0 1 2 ...
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getAppointDateString(String type, int num){
		if (type==null || type.equals("")) {
			return null;
		}
		Date date=new Date();
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(date); 
		int type_ = type.equals("DATE")?calendar.DATE:type.equals("HOUR")?calendar.HOUR:calendar.MINUTE;
		calendar.add(type_,num); //把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 字符串转换成日期 
	 * @param dateStr
	 * @param method
	 * @return
	 */
	public static Date strToDate(String dateStr, String method) { 
		Date date = null; 

		method = StringUtils.isBlank(method)?"yyyy-MM-dd":method;
	    SimpleDateFormat format = new SimpleDateFormat(method); 
	    try { 
	    	date = format.parse(dateStr); 
	    } catch (Exception e) { 
	    	e.printStackTrace(); 
	    } 
	    
	    return date; 
	} 
	
	public static String dateToStr(Date date) {
		if(date == null) return null;
		DateFormat df = new SimpleDateFormat(simple);
    	return df.format(date); 
	}
	
}
