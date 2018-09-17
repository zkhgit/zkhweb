package zkh.tool.path;

import java.util.Calendar;

/**
 * 日期路径
 *
 * 赵凯浩
 * 2018年9月17日 上午10:59:57
 */
public class Date {
	
	/**
	 * 通过日期及特定yyy-MM-dd格式返回日期文件夹路径
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String path(java.util.Date date, String format) {
		// 获取Calendar
		Calendar calendar = Calendar.getInstance();
		if(date!=null) {calendar.setTime(date);}
		String path = "";
		// 文件夹拼接
		if(format == null) {
		}else if(format.indexOf("dd")>-1 || format.indexOf("DD")>-1) {
			path = "/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
		}else if(format.indexOf("mm")>-1 || format.indexOf("MM")>-1) {
			path = "/" +calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1);
		}else if(format.indexOf("yyyy")>-1 || format.indexOf("YYYY")>-1) {
			path = "/" + calendar.get(Calendar.YEAR);
		}
		
		return path;
	}
	
}
