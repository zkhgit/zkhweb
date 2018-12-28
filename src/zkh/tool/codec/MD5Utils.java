package zkh.tool.codec;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5单向加密
 * 描述：生成32位MD5码
 *
 * 赵凯浩
 * 2018年9月17日 下午5:28:18
 */
public class MD5Utils {
	
	private Logger logger = LoggerFactory.getLogger(MD5Utils.class);
	
	private String inStr;
	private MessageDigest md5;

	public MD5Utils(String inStr) {
		this.inStr = inStr;
		try {
			this.md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			logger.error("MD5加密：创建MD5Utils实例化对象失败");
		}
	}

	public String md5Encode() {
		char[] charArray = this.inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		
		for (int i = 0; i < charArray.length; i++) {			
			byteArray[i] = (byte) charArray[i];
		}
		
		byte[] md5Bytes = this.md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");				
			}
			hexValue.append(Integer.toHexString(val));
		}
		
		return hexValue.toString();
	}

	public static void main(String[] args) {
		MD5Utils md5 = new MD5Utils("123456");
		String postString = md5.md5Encode();
		System.out.println("加密后的字符:" + postString + "  长度:" + postString.length());
	}
}
