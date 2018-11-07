package zkh.tool.propertie;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Properties文件操作
 *
 * 赵凯浩
 * 2018年9月14日 下午4:50:48
 */
public class PropertieUtil {

	private static Logger logger = LoggerFactory.getLogger(PropertieUtil.class);
	
	/**
	 * 通过key获取value
	 * 描述：单个值
	 * @param path 文件路径
	 * @param key
	 * @return
	 */
	public static String findValueByKey(String path, String key) {
        try {     
    		InputStream is = new FileInputStream(path); 
    		Properties properties = new Properties();
            properties.load(is);
            return properties.getProperty(key);
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	/**
	 * 通过keys获取values
	 * 描述：多个值
	 * @param path 文件绝对路径
	 * @param key
	 * @return
	 */
	public static String[] findValueByKeys(String path, String[] keys) {
        try {     
        	String[] values = new String[keys.length];
    		InputStream is = new FileInputStream(path); 
    		Properties properties = new Properties();
            properties.load(is);
            for (int i = 0; i < keys.length; i++) {
            	values[i] = properties.getProperty(keys[i]);
			}
            return values;
        } catch (Exception e) {
        	logger.error("属性文件操作：通过key读取value失败");
        	return null;
        }
	}
	
}
