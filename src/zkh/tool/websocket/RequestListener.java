package zkh.tool.websocket;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * 解决websocket获取不到Httpsession的问题（获取shrio的session时不用这个）
 *
 * 赵凯浩
 * 2019年1月8日 下午2:28:55
 */
// @Component
public class RequestListener implements ServletRequestListener {
	
	  @Override
	  public void requestInitialized(ServletRequestEvent sre) {
		  // 将所有request请求都携带上httpSession
		  ((HttpServletRequest) sre.getServletRequest()).getSession();
	  }
	
	  public RequestListener() {}
	
	  @Override
	  public void requestDestroyed(ServletRequestEvent arg0) {}
	  
}