package zkh.tool.excel.common;

import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel描述注解
 *
 * 赵凯浩
 * 2018年11月9日 下午4:41:47
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

	/**
	 * 列索引
	 * 描述：下标从0开始，Excel中每一行标记
	 * @return
	 */
	public int column() default 0;
	
	/**
	 * 列名
	 * @return
	 */
	public String name() default "";
	
	/**
	 * 类型
	 * @return
	 */
//	public String type() default "String";
	public Class<?> type() default Class.class;
	
	/**
	 * 最小长度
	 * @return
	 */
	public int minLength() default -1;
	
	/**
	 * 最大长度
	 * @return
	 */
	public int maxLength() default -1;
	
	/**
	 * 是否必须
	 * @return
	 */
	public boolean required() default false;
	
}
