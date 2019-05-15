package zkh.tool.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

import zkh.tool.bean.ResultData;
import zkh.tool.bean.UserInfo;
import zkh.tool.date.DateUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * 消息推送服务
 *
 * 赵凯浩
 * 2019年1月25日 上午9:13:26
 */
@ServerEndpoint(value = "/msgPushServer", configurator = WebSocketConfiguration.class)
@Component
public class MsgPushServer {
	
	private static Logger logger = LoggerFactory.getLogger(MsgPushServer.class);
	
	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static CopyOnWriteArraySet<MsgPushServer> webSocketSet = new CopyOnWriteArraySet<MsgPushServer>();
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	// 与某个客户端的连接会话，需要通过它来给客户端发送(消息)数据
	private Session webSocketSession;
	// 当前用户（貌似每连接一个用户就会单独使用一个WebSocketServer实例，而在用户的本次会话中可以随时调用WebSocketServer实例的属性）(此处只存放主要几个用户属性)
    private UserInfo userInfo;
    // 当前用户sessionId
    private String sessionId;
	// 当前所有用户（这个其实可以说是上面webSocketSet属性的简化版）
	private static List<UserInfo> userList = new ArrayList<UserInfo>();

	/**
	 * 连接建立成功调用的方法
	 * @param webSocketSession
	 * @param config 存储着当前用户的一些属性
	 */
	@OnOpen
	public void onOpen(Session webSocketSession, EndpointConfig config) {
		this.webSocketSession = webSocketSession;
		this.sessionId = (String) config.getUserProperties().get("sessionId");
		// 通过EndpointConfig获取HttpSession，再通过HttpSession获取当前用户（保存当前用户的主要属性，这里去掉了密码）
		this.userInfo = (UserInfo) config.getUserProperties().get("userInfo");
		this.userInfo.setPassword(null);
		this.userInfo.setLoginIp(this.sessionId);
		if(this.userInfo!=null) {			
			try {
				// 保存当前用户到list
				userList.add(this.userInfo);				
				// 给websocket中添加当前用户的WebSocketServer
				webSocketSet.add(this);
				// 在线人数加1
				addOnlineCount();
				logger.debug("有新加入webSocketServer！当前在线人数为" + getOnlineCount());
			} catch (Exception e) {
				logger.error("webSocketServer用户连接失败");
				e.printStackTrace();
			}
		}else {
			logger.debug("webSocketServer用户未登录");
		}
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		
	}
	
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		try {
			// 从用户列表移除当前用户
			userList.remove(this.userInfo);
			// 从websocket移除当前用户
			webSocketSet.remove(this);
			// 在线人数减1
			subOnlineCount();       
			logger.debug("有一webSocketServer连接关闭！当前在线人数为" + getOnlineCount());
		} catch (Exception e) {
			logger.error("webSocketServer用户下线时出错");
			e.printStackTrace();
		}
	}

	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable t) {
		logger.error("webSocketServer发生错误");
		t.printStackTrace();
	}

	/**
	 * 推送消息给前端
	 * 消息类型：字符串
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws Exception {
		this.webSocketSession.getBasicRemote().sendText(message);
		//this.session.getAsyncRemote().sendText(message);
	}
	
	/**
	 * 推送消息给前端
	 * 消息类型：obj
	 * @param obj
	 * @throws IOException
	 */
	public void sendMessage(Object obj) throws Exception {
		this.sendMessage(JSONObject.toJSONString(obj));
	}

	/**
	 * 给所有人发消息
	 * @param message
	 * @throws Exception
	 */
	public void sendAll(String message) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sendTime", getDateTime(null));
		map.put("onlineCount", onlineCount);
		map.put("sessionId", this.sessionId);
		map.put("userInfo", this.userInfo);
		map.put("userList", userList);
		for (MsgPushServer item : webSocketSet) {
			item.sendMessage(ResultData.success(message, map));
		}
	}
	
	/**
	 * 发送消息给指定用户
	 * @param message
	 * @param userId
	 * @throws Exception
	 */
	public void sendToUser(String message, String userId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sendTime", getDateTime(null));
		map.put("onlineCount", onlineCount);
		map.put("sessionId", this.sessionId);
		map.put("userInfo", this.userInfo);
		map.put("userList", userList);
		for (MsgPushServer item : webSocketSet) {
			if(userId.equals(this.userInfo.getUserId())) {				
				item.sendMessage(ResultData.success(message, map));
			}
		}
	}
	
	/**
	 * 发送消息给一些用户
	 * @param message
	 * @param userIds
	 * @throws Exception
	 */
	public void sendToUsers(String message, String[] userIds, UserInfo userInfo) throws Exception{
		userInfo = userInfo==null?this.userInfo:userInfo;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userInfo", userInfo);
		String dateTime = DateUtil.dateToStr(new Date(), null);
		for (MsgPushServer item : webSocketSet) {
			for (int i = 0; i < userIds.length; i++) {				
				if(userIds[i].equals(item.userInfo.getUserId())) {
					item.sendMessage(ResultData.success("<span style='font-size:10px;color:gray'>[" + dateTime + "]</span>&nbsp;<span style='color:blue;'>" + userInfo.getAccount() + "@你：</span>" + message, map));
				}
			}
		}
	}
	
	/**
	 * 获取当前时间
	 * @param date
	 * @return
	 */
	private String getDateTime(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = date != null ? date : new Date();
		return format.format(date);
	}
	public static synchronized int getOnlineCount() {
		return onlineCount;
	}
	public static synchronized void addOnlineCount() {
		MsgPushServer.onlineCount++;
	}
	public static synchronized void subOnlineCount() {
		MsgPushServer.onlineCount--;
	}
}