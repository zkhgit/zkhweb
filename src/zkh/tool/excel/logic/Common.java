package zkh.tool.excel.logic;

/**
 * 公用方法
 *
 * 赵凯浩
 * 2018年12月3日 下午4:03:19
 */
public class Common {

	/**
	 * 获得属性的get/set方法名称
	 * @param fieldName 属性名称
	 * @param getOrSet "get"或"set"
	 * @return
	 */
	public static String getMethodName(String fieldName, String getOrSet) {
		// 大写的属性首字母
		String initials = fieldName.substring(0, 1).toUpperCase();
		// 属性从第二位开始的名称
		String surplus = fieldName.length() > 1 ? fieldName.substring(1) : "";
		// 属性的get方法名称
		String getMethod = getOrSet + initials + surplus; 
		return getMethod;
	}
	
}
