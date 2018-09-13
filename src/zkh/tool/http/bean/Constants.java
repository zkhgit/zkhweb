package zkh.tool.http.bean;

/**
 * HTTP相关常量
 *
 * 赵凯浩
 * 2018年8月15日 下午3:16:52
 */
public class Constants {
	
	/**
	 * 请求方式
	 */
	public final static String GET = "GET";
	public final static String POST = "POST";
	
	/**
	 * 实体头、请求头设置
	 */
	public final static String ACCEPT = "Accept";
	public final static String CHARSET_VALUE = "UTF-8";
	public final static String CONTENT_TYPE_CHARSET = ";charset=" + CHARSET_VALUE;
	
	public final static String APPLICATION_JSON = "application/json";
	public final static String CONTENT_TYPE = APPLICATION_JSON + CONTENT_TYPE_CHARSET;
	
}
