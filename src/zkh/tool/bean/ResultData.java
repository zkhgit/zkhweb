package zkh.tool.bean;

/**
 * 请求返回的结果对象
 *
 * 赵凯浩
 * 2018年12月24日 下午3:13:56
 */
public class ResultData {
	
	private String msg = null; // 提示信息
	private Object obj = null; // 其他信息
	private boolean success = false; // 成功或失败
	private static ResultData resultData = new ResultData(); // 公用对象

	// 无参
	public ResultData() {
		super();
	}

	// 全参
	public ResultData(String msg, Object obj, boolean success) {
		super();
		this.msg = msg;
		this.obj = obj;
		this.success = success;
	}
	
	// 失败-简化版
	public static ResultData fail(String msg) {
		resultData.setSuccess(false);
		resultData.setMsg(msg);
		return resultData;
	}
	
	// 失败-正式版
	public static ResultData fail(String msg, Object obj) {
		resultData.setSuccess(false);
		resultData.setMsg(msg);
		resultData.setObj(obj);
		return resultData;
	}
	
	// 成功-简化版
	public static ResultData success(String msg) {
		resultData.setSuccess(true);
		resultData.setMsg(msg);
		return resultData;
	}
	
	// 成功-简化版
	public static ResultData success(Object obj) {
		resultData.setSuccess(true);
		resultData.setObj(obj);;
		return resultData;
	}
	
	// 成功-正式版
	public static ResultData success(String msg, Object obj) {
		resultData.setSuccess(true);
		resultData.setMsg(msg);
		resultData.setObj(obj);
		return resultData;
	}
	

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

}
