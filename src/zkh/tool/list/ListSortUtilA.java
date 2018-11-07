package zkh.tool.list;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 排序
 * 描述：单个字段排序
 *
 * 赵凯浩
 * 2018年11月7日 上午8:22:56
 */
public class ListSortUtilA<T> {
	
	private Logger logger = LoggerFactory.getLogger(ListSortUtilA.class);
	
	@SuppressWarnings("all")
	public void Sort(List<T> list, final String propName, final String sort) {
        Collections.sort(list, new Comparator() {
        	@Override
        	public int compare(Object obj1, Object obj2) {
                int ret = 0;
                
                try {
	                if(StringUtils.isNotBlank(propName)) {
	                	// 获取长度
	                	int len = propName.length();
	                	// 第一个字符大写
	                	String first = (propName.charAt(0) + "").toUpperCase();
	                	// 获取属性的get方法名
	                	String getPropName = "get" + (len>1?first+propName.substring(1):first);
	                	
	                	Method m1 = ((T) obj1).getClass().getMethod(getPropName, null);
	                	Method m2 = ((T) obj2).getClass().getMethod(getPropName, null);
	                	
	                	Object object1 = m2.invoke(((T) obj1), null);
	                	Object object2 = m2.invoke(((T) obj2), null);
	                	
	                	if (sort != null && "desc".equals(sort.toLowerCase())) {
	                		// 倒序
	                		ret = retDesc(object1, object2);
	                	}else {                    	
	                		// 正序
	                		ret = retAsc(object1, object2);
	                	}
	                }else {
	                	logger.error("List排序异常：排序字段不能为空");
	                }
                } catch (Exception e) {
                	logger.error("List排序异常：[ "+propName+" ]");
                	e.printStackTrace();
                }
                
                return ret;
            }
        });
    }

	/**
	 * 生成ret-倒序
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 * @throws Exception
	 */
	protected static int retDesc(Object object1, Object object2) throws Exception{
		int ret = 0;
		
		if(object1 == null) {
			ret = -1;
		}else if(object2 == null) {
			ret = 1;
		}else if(object1 instanceof Byte){
			Byte obj1 = ((Byte)object1);
			Byte obj2 = ((Byte)object2);
			ret = obj2.compareTo(obj1);
		}else if(object1 instanceof Short){
			Short obj1 = ((Short)object1);
			Short obj2 = ((Short)object2);
			ret = obj2.compareTo(obj1);
		}else if(object1 instanceof Integer){
			Integer obj1 = ((Integer)object1);
			Integer obj2 = ((Integer)object2);
			ret = obj2.compareTo(obj1);
		}else if(object1 instanceof Long){
			Long obj1 = ((Long)object1);
			Long obj2 = ((Long)object2);
			ret = obj2.compareTo(obj1);
		}else if(object1 instanceof Float){
			Float obj1 = ((Float)object1);
			Float obj2 = ((Float)object2);
			ret = obj2.compareTo(obj1);
		}else if(object1 instanceof Double){
			Double obj1 = ((Double)object1);
			Double obj2 = ((Double)object2);
			ret = obj2.compareTo(obj1);
		}else if(object1 instanceof java.util.Date){
			Long obj1 = ((Date)object1).getTime();
			Long obj2 = ((Date)object2).getTime();
			ret = obj2.compareTo(obj1);
		}else {
			ret = object2.toString().compareTo(object1.toString());                    		                			
		}
		
		return ret;
	}
	
	/**
	 * 生成ret-正序
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 * @throws Exception
	 */
	protected static int retAsc(Object object1, Object object2) throws Exception{
		int ret = 0;
		
		if(object1 == null) {
			ret = 1;
		}else if(object2 == null) {
			ret = -1;
		}else if(object1 instanceof Byte){
			Byte obj1 = ((Byte)object1);
			Byte obj2 = ((Byte)object2);
			ret = obj1.compareTo(obj2);
		}else if(object1 instanceof Short){
			Short obj1 = ((Short)object1);
			Short obj2 = ((Short)object2);
			ret = obj1.compareTo(obj2);
		}else if(object1 instanceof Integer){
			Integer obj1 = ((Integer)object1);
			Integer obj2 = ((Integer)object2);
			ret = obj1.compareTo(obj2);
		}else if(object1 instanceof Long){
			Long obj1 = ((Long)object1);
			Long obj2 = ((Long)object2);
			ret = obj1.compareTo(obj2);
		}else if(object1 instanceof Float){
			Float obj1 = ((Float)object1);
			Float obj2 = ((Float)object2);
			ret = obj1.compareTo(obj2);
		}else if(object1 instanceof Double){
			Double obj1 = ((Double)object1);
			Double obj2 = ((Double)object2);
			ret = obj1.compareTo(obj2);
		}else if(object1 instanceof Date){
			Long obj1 = ((Date)object1).getTime();
			Long obj2 = ((Date)object2).getTime();
			ret = obj1.compareTo(obj2);
		}else {	 
			ret = object1.toString().compareTo(object2.toString());                    		                			
		}
		
		return ret;
	}
	
}
