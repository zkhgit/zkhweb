package zkh.tool.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;

/**
 * 利用反射操作Object对象
 *
 * 赵凯浩
 * 2019年1月23日 上午10:11:25
 */
public class BeanUtil {
	
	/**
	 * 两个对象，用newObj覆盖对象oldObj
	 * 描述：如果newObj的属性值不为null，则将它的属性值赋给oldObj
	 * 注意：List目前还没有测试，慎用
	 * @param oldObj
	 * @param newObj
	 * @return oldObj
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static Object updateObject(Object oldObj, Object newObj) throws Exception {
		Object oldFieldVal = null, newFieldVal = null; Class<?> fieldClass = null; Method method = null; String fieldName = null;
		Class<?> clazz = newObj.getClass();
		oldObj = JSONObject.parseObject(JSONObject.toJSONString(oldObj), clazz);
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			fieldName = field.getName();
			try {
				clazz.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
			} catch (Exception e) {
				// 普通属性不执行下面的操作
				continue;				
			}
			// 属性值
			oldFieldVal = field.get(oldObj);
			newFieldVal = field.get(newObj);
			if(newFieldVal == null && oldFieldVal == null) {
				// 两个都为null
			}else if (newFieldVal != null && oldFieldVal == null) {
				fieldClass = Class.forName(newFieldVal.getClass().toString().substring(6));
				// 新的不为null，旧的为null
				field.set(oldObj, field.get(newObj));
			}else if(newFieldVal != null && oldFieldVal != null) {
				// 都不为null
				fieldClass = Class.forName(newFieldVal.getClass().toString().substring(6));
				if(field.getType() == java.util.List.class || field.getType() == java.util.Set.class) {
					// 都不为null，且是list或set
					List<Object> tempList = new ArrayList<Object>();
					List<Object> oldList = null;
					List<Object> newList = null;
					if(field.getType() == java.util.List.class) {
						oldList = (List<Object>) oldFieldVal;
						newList = (List<Object>) newFieldVal;
					}else {
						oldList = new ArrayList<Object>((Collection<? extends Object>) oldFieldVal);
						newList = new ArrayList<Object>((Collection<? extends Object>) newFieldVal);
					}
					Object oldTemp = null; Object newTemp = null;
					for (int j = 0; j < oldList.size(); j++) {
						// 旧的List属性里的一个对象
						oldTemp = oldList.get(j);
						boolean flag = false; int index = -1;
						for (int j2 = 0; j2 < newList.size(); j2++) {
							// 新的List属性里的一个对象
							newTemp = newList.get(j2);
							// 通过在自定义类中的importEquals方法判断两个对象是否相等
							method = newTemp.getClass().getMethod("importEquals",newTemp.getClass(),newTemp.getClass());
							// 如果自定义对比相同的方法不存在,则不做对比，直接用新的替换旧的
							if(method == null) {
								if(field.getType() == java.util.Set.class) {
									field.set(oldObj, new HashSet<Object>(newList));
								}else {
									field.set(oldObj, newList);
								}
								break;
							}
							flag = (boolean) method.invoke(newTemp,oldTemp,newTemp);
							if(flag) {
								oldTemp = updateObject(oldTemp, newTemp);
								oldList.set(j, oldTemp);
								index = j2;
								break;
							}
						}
						if(flag = false) {
							tempList.add(newList.get(index));
						}
					}
					HashSet<Object> setTemp = new HashSet<Object>(tempList);
					HashSet<Object> setTempNew = new HashSet<Object>(newList);
					setTempNew.removeAll(setTemp);
					newList.clear();
					newList.addAll(setTempNew);
					oldList.addAll(newList);
					if(field.getType() == java.util.Set.class) {
						field.set(oldObj, new HashSet<Object>(oldList));
					}else {
						field.set(oldObj, oldList);
					}
				}else if(fieldClass.getAnnotation(Table.class) != null) {
					// 都不为null，且是自定义类型
					oldFieldVal = updateObject(oldFieldVal, newFieldVal);
					field.set(oldObj, oldFieldVal);
				}else {
					// 基本类型
					field.set(oldObj, field.get(newObj));
				}
			}
		}
		return oldObj;
	}
	
	/**
	 * 通过属性名获得属性的get/set方法名称
	 * @param fieldName 属性名称
	 * @param getOrSet "get"或"set"
	 * @return
	 */
	public static String getMethodName(String fieldName, String getOrSet) throws Exception {
		// 大写的属性首字母
		String initials = fieldName.substring(0, 1).toUpperCase();
		// 属性从第二位开始的名称
		String surplus = fieldName.length() > 1 ? fieldName.substring(1) : "";
		// 属性的get方法名称
		String getMethod = getOrSet + initials + surplus; 
		return getMethod;
	}
	
	/**
	 * 通过属性名获得Get方法
	 * @param fieldName 属性名
	 * @param clazz 当前类的Class
	 * @return
	 * @throws Exception
	 */
	public static Method getGetMethodByFieldName(Class<?> clazz, String fieldName) throws Exception {
		// 属性的get方法名称
		String getMethodName = getMethodName(fieldName, "get");
		// get方法
		Method getMethod = clazz.getMethod(getMethodName);
		return getMethod;
	}
	
	/**
	 * 通过属性名获得Set方法
	 * @param fieldName 属性名
	 * @param clazz 当前类的Class
	 * @return
	 * @throws Exception
	 */
	public static Method getSetMethodByFieldName(Class<?> clazz, String fieldName) throws Exception {
		// 属性的get方法名称
		String getMethodName = getMethodName(fieldName, "get");
		// get方法
		Method getMethod = clazz.getMethod(getMethodName);
		// get方法返回类型
		Class<?> fieldMethodReturnClass = getMethod.getReturnType();
		// 属性的set方法名称
		String setMethodName = getMethodName(fieldName, "set");
		// set方法
		Method setMethod = clazz.getMethod(setMethodName, new Class[]{fieldMethodReturnClass});
		return setMethod;
	}

	/**
     * 通过对象和属性名获取属性值
     * @param obj
     * @param key
     * @return
     * @throws Exception 
     */
	public static Object getValueByKey(Object obj, String key) throws Exception {
    	Class<?> clazz = obj.getClass();
    	// 重组正确的Class（过滤类名后带着的类_$$_jvstfaa_1字符串，针对Springboot）
    	String clazzStr = clazz.toString();
    	int _len = clazzStr.indexOf("_");
    	if(_len>0) {
    		clazz = Class.forName(clazzStr.substring(0, _len).replace("class ", ""));    		    		
    	}
    	// 获得属性的get方法
    	Method method = BeanUtil.getGetMethodByFieldName(clazz, key);
        Object value = method.invoke(obj, new Object[] {});    
        return value;
    }
}