package zkh.tool.websocket;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import zkh.tool.bean.UserInfo;

/**
 * 解决WebSocket获取不到Session（包括HttpSession、shiro的session等）的问题
 * 描述：配置在@ServerEndpoint(value="/websocket",configurator=WebSocketConfiguration.class)
 * 
 * 赵凯浩
 * 2019年1月7日 上午10:04:39
 */
@Configuration
public class WebSocketConfiguration extends ServerEndpointConfig.Configurator {

	/**
	 * 获取session
	 * 描述：修改握手(就是在握手协议建立之前修改其中携带的内容) 
	 * 为解决主动推送的难题，需要在建立连接时，将websocket下的session与servlet下的HttpSession（或者其他session，比如shiro下的session）建立关联关系
	 */
	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		/*如果没有ServletRequest监听器，那么这里获取到的HttpSession（获取shiro的session时不需要ServletRequest监听器）是null*/
		sec.getUserProperties().put("sessionId", "123");
        sec.getUserProperties().put("userInfo", new UserInfo()); // sec.getUserProperties() webSocket提供的用于存放当前用户信息的，此处存储了当前登录用户
	}
	
	@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        // 貌似只有服务器是tomcat的时候才需要配置
        return new ServerEndpointExporter();
    }
	
}