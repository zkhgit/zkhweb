package zkh.tool.reflex.bean;

/**
 * Object对象属性的属性
 *
 * 赵凯浩
 * 2018年11月6日 下午3:43:15
 */
public class Field {

	private String name; // 属性名 
	private Class<?> type; // 属性类型
	private Object value; // 属性值
	
	public Field() {
		super();
	}
	public Field(String name, Class<?> type, Object value) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
