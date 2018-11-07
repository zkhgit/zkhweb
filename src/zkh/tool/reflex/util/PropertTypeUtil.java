package zkh.tool.reflex.util;

import java.util.Date;

import zkh.tool.date.DateUtil;

/**
 * Date类型处理
 *
 * 赵凯浩
 * 2018年11月6日 下午5:04:30
 */
public class PropertTypeUtil {
	
	/**
	 * 判断是否为java.util.Date类型
	 * @param object
	 * @param className
	 * @return
	 */
	public static Object isDateType(Object object, Class<?> className) {
		try {
			return className.toString().equals("class java.util.Date")?DateUtil.getSimpleTime((Date) object):object.toString();
		} catch (Exception e) {
			return null;
		}
	}

}
