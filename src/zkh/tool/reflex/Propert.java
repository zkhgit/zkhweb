package zkh.tool.reflex;

import java.lang.reflect.Field;

import zkh.tool.reflex.common.PropertTypeUtil;

/**
 * 反射-获取属性值
 *
 * 赵凯浩
 * 2018年11月6日 下午3:41:23
 */
public class Propert {

	/**
	 * 通过属性名获取Object对象的属性值
	 * @param object
	 * @param attrName
	 * @return
	 */
	public static Object getValueByAttrName(Object object, String attrName) {
		Object value = null;
		
	    try {
	        // 通过属性获取对象的属性
	        Field field = object.getClass().getDeclaredField(attrName);
	        // 对象的属性的访问权限设置为可访问
	        field.setAccessible(true);
	        // 获取属性的对应的值
	        // java.util.Date类型处理
	        PropertTypeUtil.isDateType(field.get(object), field.getType());
	        value = field.get(object).toString();
	    } catch (Exception e) {
	        return null;
	    }
	     
	    return value;
	}
	
	/**
	 * 通过属性名获取Object对象的属性值
	 * 描述：返回自定义类型Field
	 * @param object
	 * @param attrName
	 * @return
	 */
	public static zkh.tool.reflex.bean.Field getValueByAttrNameField(Object object, String attrName) {
		zkh.tool.reflex.bean.Field value = null;
		Object temp = null;
		
	    try {
	        // 通过属性获取对象的属性
	        Field field = object.getClass().getDeclaredField(attrName);
	        // 对象的属性的访问权限设置为可访问
	        field.setAccessible(true);
	        // java.util.Date类型处理
	        temp = PropertTypeUtil.isDateType(field.get(object), field.getType());
	        value = new zkh.tool.reflex.bean.Field(attrName ,field.getType() ,temp);
	    } catch (Exception e) {
	        return null;
	    }
	     
	    return value;
	}
	
	/**
	 * 通过属性名获取Object对象的属性值
	 * 描述：返回String类型
	 * @param object
	 * @param attrName
	 * @return
	 */
	public static String getValueByAttrNameString(Object object, String attrName) {
		String value = null;
		
	    try {
	        // 通过属性获取对象的属性
	        Field field = object.getClass().getDeclaredField(attrName);
	        // 对象的属性的访问权限设置为可访问
	        field.setAccessible(true);
	        // 获取属性的对应的值
	        value = field.get(object).toString();
	    } catch (Exception e) {
	        return null;
	    }
	     
	    return value;
	}
	
	/**
	 * 通过属性名获取Object对象的属性值
	 * 描述：返回int类型
	 * @param object
	 * @param attrName
	 * @return
	 */
	public static int getValueByAttrNameInt(Object object, String attrName) {
		int value = 0;
		
	    try {
	        // 通过属性获取对象的属性
	        Field field = object.getClass().getDeclaredField(attrName);
	        // 对象的属性的访问权限设置为可访问
	        field.setAccessible(true);
	        // 获取属性的对应的值
	        value = (int) field.get(object);
	    } catch (Exception e) {
	        return 0;
	    }
	     
	    return value;
	}
	
	/**
	 * 通过属性名获取Object对象的属性值
	 * 描述：返回long类型
	 * @param object
	 * @param attrName
	 * @return
	 */
	public static long getValueByAttrNameLong(Object object, String attrName) {
		long value = 0;
		
	    try {
	        // 通过属性获取对象的属性
	        Field field = object.getClass().getDeclaredField(attrName);
	        // 对象的属性的访问权限设置为可访问
	        field.setAccessible(true);
	        // 获取属性的对应的值
	        value = (long) field.get(object);
	    } catch (Exception e) {
	        return 0;
	    }
	     
	    return value;
	}
	
	/**
	 * 通过属性名获取Object对象的属性值
	 * 描述：返回float类型
	 * @param object
	 * @param attrName
	 * @return
	 */
	public static float getValueByAttrNameFloat(Object object, String attrName) {
		float value = 0;
		
	    try {
	        // 通过属性获取对象的属性
	        Field field = object.getClass().getDeclaredField(attrName);
	        // 对象的属性的访问权限设置为可访问
	        field.setAccessible(true);
	        // 获取属性的对应的值
	        value = (float) field.get(object);
	    } catch (Exception e) {
	        return 0;
	    }
	     
	    return value;
	}
	
	/**
	 * 通过属性名获取Object对象的属性值
	 * 描述：返回double类型
	 * @param object
	 * @param attrName
	 * @return
	 */
	public static double getValueByAttrNameDouble(Object object, String attrName) {
		double value = 0;
		
	    try {
	        // 通过属性获取对象的属性
	        Field field = object.getClass().getDeclaredField(attrName);
	        // 对象的属性的访问权限设置为可访问
	        field.setAccessible(true);
	        // 获取属性的对应的值
	        value = (double) field.get(object);
	    } catch (Exception e) {
	        return 0;
	    }
	     
	    return value;
	}
	
}