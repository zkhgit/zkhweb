package zkh.tool.codec;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
 
import org.apache.commons.codec.binary.Base64;
 
/**
 * AES对称加密算法
 *
 * 赵凯浩
 * 2018年9月17日 下午5:28:11
 */
public class AESUtil {
    //密钥算法
    public static final String KEY_ALGORITHM = "AES";
     
    //加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
     
    /**
     * 生成密钥
     */
    public static String initkey() throws Exception{
    	// 实例化密钥生成器
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器:AES要求密钥长度为128,192,256位
        kg.init(128);                                              
        // 生成密钥
        SecretKey secretKey = kg.generateKey();                  
        // 获取二进制密钥编码形式
        return Base64.encodeBase64String(secretKey.getEncoded());
    }
     
    /**
     * 转换密钥
     */
    public static Key toKey(byte[] key) throws Exception{
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }
     
    /**
     * 加密数据
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的数据
     * */
    public static String encrypt(String data, String key) throws Exception{
    	// 还原密钥
        Key k = toKey(Base64.decodeBase64(key));                      
        /** 
         * 实例化Cipher对象，它用于完成实际的加密操作
         * 如果使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)
         * Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
         */
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);              
        // 初始化Cipher对象，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);                              
        // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));
    }
     
    /**
     * 解密数据
     * @param data 待解密数据
     * @param key  密钥
     * @return 解密后的数据
     * */
    public static String decrypt(String data, String key) throws Exception{
        Key k = toKey(Base64.decodeBase64(key));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);                         
        // 执行解密操作
        return new String(cipher.doFinal(Base64.decodeBase64(data)));
    }
    
    
    public static void main(String[] args) throws Exception {
    	String key =  AESUtil.initkey();
    	String dString = "cepsp2";
    	String mima = AESUtil.encrypt(dString,key);
    	
    	System.out.println(key);
    	System.out.println(mima);
    	System.out.println(decrypt("am+koOAEGSMLYW8JojzaPg==","V2IOJ8dWQppjLc7usJv5Jg=="));
    	//1--key:3NYnv5izdgIZ1274mDo0TQ==  ;;密码：K7w64CqCOd2cNrybwxKvdg==
    	//2--key:kNj+m9lQRk9rtAS57oMiAw==  ;;密码: tQhqjID9kwN7mffmyxt23A==
	}
}
