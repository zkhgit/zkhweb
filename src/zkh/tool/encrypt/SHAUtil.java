package zkh.tool.encrypt;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SHA单向加密
 * 描述：生成40位SHA码
 *
 * 赵凯浩
 * 2018年9月17日 下午5:28:46
 */
public class SHAUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(SHAUtil.class);
	
    /*** 
     * SHA加密 生成40位SHA码
     * @param 待加密字符串
     * @return 返回40位SHA码
     */
    public static String shaEncode(String inStr) {
    	try {			
    		MessageDigest sha = null;
    		try {
    			sha = MessageDigest.getInstance("SHA");
    		} catch (Exception e) {
    			System.out.println("加密用户密码失败！！");
    			return "";
    		}
    		
    		byte[] byteArray = inStr.getBytes("UTF-8");
    		byte[] md5Bytes = sha.digest(byteArray);
    		StringBuffer hexValue = new StringBuffer();
    		for (int i = 0; i < md5Bytes.length; i++) {
    			int val = ((int) md5Bytes[i]) & 0xff;
    			if (val < 16) { 
    				hexValue.append("0");
    			}
    			hexValue.append(Integer.toHexString(val));
    		}
    		return hexValue.toString();
		} catch (Exception e) {
			logger.error("SHA加密：加密失败");
			return null;
		}
    }
    
    
    public static void main (String [] args){
    	try {
			System.out.println(SHAUtil.shaEncode("888888"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
