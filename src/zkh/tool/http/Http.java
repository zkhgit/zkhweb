package zkh.tool.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;
import zkh.tool.http.bean.Constants;

/**
 * HTTP 请求工具
 *
 * 赵凯浩
 * 2018年8月15日 上午10:42:06
 */
public class Http {
    
	/**
     * 向指定URL发送GET方式的请求
     * 
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static JSONObject get(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = StringUtils.isBlank(param) ? url : (url + "?" + param);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return JSONObject.fromObject(result);
    }

 
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static JSONObject post(String url, String param) {
    	PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        
        try {
        
        	URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            // 发送请求参数
            if(!StringUtils.isBlank(param)) {            	
            	out.print(param);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            
        } catch (Exception e) {
        	System.out.println("发送 POST 请求出现异常！"+e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            
        	try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        	
        }
        
        return JSONObject.fromObject(result);
    }
    
    
    /**
     * 向指定URL 发送POST方法的请求
     * 
     * @param url
     * @param json
     * @return
     */
    public static JSONObject postJson(String url, JSONObject json) {
    	JSONObject jsonObject = new JSONObject();
    	
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		
		// Http报头分为通用报头，请求报头，响应报头和实体报头
		// 请求方的http报头结构：通用报头|请求报头|实体报头 
		// 响应方的http报头结构：通用报头|响应报头|实体报头
		try {
			// 参数不为空时头部信息配置
			if(null != json) {	
				// 实体头（代表发送端发送的数据格式是json类型）
				httpPost.addHeader(HTTP.CONTENT_TYPE, Constants.CONTENT_TYPE);
				// 请求头（代表客户端希望接受的数据类型是json类型）
				httpPost.setHeader(Constants.ACCEPT, Constants.APPLICATION_JSON);
				// 以上两行代表希望接受的数据类型是json格式，本次请求发送的数据的数据格式是json。
				
				// 设置请求参数及其编码类型
				httpPost.setEntity(new StringEntity(json.toString(), Charset.forName(Constants.CHARSET_VALUE)));
			}
			
			// 发送Post,并返回一个response对象
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					jsonObject = JSONObject.fromObject(EntityUtils.toString(entity, Constants.CHARSET_VALUE));
					return jsonObject;
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			System.out.println("发送HTTP_POST【postJson】，请求异常" + e);
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jsonObject;
	}
    
}