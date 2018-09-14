package zkh.tool.propertie;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties文件操作
 *
 * 赵凯浩
 * 2018年9月14日 下午4:50:48
 */
public class PropertieUtil {

	/**
	 * 通过key获取value
	 * 
	 * @param path 文件路径
	 * @param key
	 * @return
	 */
	public static String findValueByKey(String path, String key) {
        try {     
    		InputStream is = new PropertieUtil().getClass().getClassLoader().getResourceAsStream(path);
    		Properties properties = new Properties();
            properties.load(is);
            return properties.getProperty(key);
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
}