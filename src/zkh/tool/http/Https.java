package zkh.tool.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;
import zkh.tool.http.bean.Constants;

/**
 * HTTPS 请求工具
 *
 * 赵凯浩
 * 2018年8月15日 上午10:41:48
 */
public class Https {
    
	/**
     * 发送https请求（GET）
     * 
     * @param url 请求地址
     * @param json 提交的数据  JSON格式
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject get(String url, JSONObject json) {
    	JSONObject jsonObject = null;
    	
    	try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url_ = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) url_.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(Constants.GET);
            // 当outputStr不为null时向输出流写数据
            if (json != null) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(json.toString().getBytes(Constants.CHARSET_VALUE));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Constants.CHARSET_VALUE);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            //将返回的结果包装成JSON字符串
            jsonObject = JSONObject.fromObject(buffer.toString());
            
        } catch (ConnectException ce) {
            System.out.println("HTTPS_GET，连接超时");
        } catch (Exception e) {
        	System.out.println("HTTPS_GET，异常："+ e);
        }
        
        return jsonObject;
    }
 
    /**
     * 发送https请求（POST）
     * 
     * @param url 请求地址
     * @param param 提交的数据    JSON格式
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject post(String url, JSONObject json) {
        
    	JSONObject jsonObject = null;
    	try {
    		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url_ = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) url_.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(Constants.POST);
            // 当outputStr不为null时向输出流写数据
            if (json != null) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(json.toString().getBytes(Constants.CHARSET_VALUE));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Constants.CHARSET_VALUE);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            //将返回的结果包装成JSON字符串
            jsonObject = JSONObject.fromObject(buffer.toString());
            
        } catch (ConnectException ce) {
            System.out.println("HTTPS_POST，连接超时");
        } catch (Exception e) {
        	System.out.println("HTTPS_POST，异常："+ e);
        }
        
        return jsonObject;
    }    
    
    /**
     * 发送https请求（GET、POST）
     * 
     * @param url 请求地址
     * @param mothod 请求方式（GET、POST），默认GET
     * @param param 提交的数据 JSON格式
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject send(String url, String mothod, JSONObject json) {
    	JSONObject jsonObject = null;

    	/*请求方式判定*/
    	if(StringUtils.isBlank(mothod)) {
    		// 默认GET请求
    		mothod = Constants.GET;
    	}else {    		
    		// 强制大写
    		mothod = mothod.toUpperCase();
    	}
    	
    	try {
        	// 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url_ = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) url_.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(mothod);
            // 当outputStr不为null时向输出流写数据
            if (json != null) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(json.toString().getBytes(Constants.CHARSET_VALUE));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Constants.CHARSET_VALUE);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            //将返回的结果包装成JSON字符串
            jsonObject = JSONObject.fromObject(buffer.toString());
            
        } catch (ConnectException ce) {
            System.out.println("HTTPS_" + mothod + "，连接超时");
        } catch (Exception e) {
        	System.out.println("HTTPS_" + mothod + "，异常："+ e);
        }
        
        return jsonObject;
    }
    
}


/**
 * 信任管理器
 * 支持发送https请求的工具 
 */
class MyX509TrustManager implements X509TrustManager {
    // 检查客户端证书
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }
    // 检查服务器端证书
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }
    // 返回受信任的X509证书数组
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
