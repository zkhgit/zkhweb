package zkh.bean;

/**
 * 结果返回对象
 *
 * 赵凯浩
 * 2018年8月15日 上午8:59:19
 */
public class DataResult {
	
	private String msg;
	private boolean success = false;
	private Object obj;
	
	public DataResult() {
		super();
	}
	
	// 失败时返回错误信息
	public DataResult(String msg) {
		super();
		this.msg = msg;
	}
	
	// 成功时返回成功提示信息及数据包obj
	public DataResult(String msg, Object obj) {
		super();
		this.msg = msg;
		this.success = true;
		this.obj = obj;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
