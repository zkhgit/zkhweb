package zkh.tool.codec;

import org.apache.shiro.crypto.hash.Md5Hash;

public class ShiroMD5 {

	public static void main(String[] args) {
		String password = "123";
		String salt = "A1B2C3D4efg.5679g8e7d6c5b4a_-=_)(8."; // 自定义 
		System.out.println(new Md5Hash(password, salt, 2).toHex()); // 原始密码，盐值、迭代次数
	}
	
}
