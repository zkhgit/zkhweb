package zkh.tool.word.logic;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import zkh.tool.bean.BeanUtil;

/**
 * POI导出格式word文件
 * 描述：可以导出doc及docx格式的文件
 *
 * 赵凯浩
 * 2019年1月30日 上午9:12:48
 */
public class WordWriter<T> {

	private static Logger logger = LoggerFactory.getLogger(WordWriter.class);

	private String [] headerNames; // 变量字段名
    private String [] fieldNames; // 数据字段名
    private List<T> data; // 当前sheet的数据list
    private String format="yyyy-MM-dd HH:mm:ss"; // 时间格式化
	private XWPFDocument document;
	public String exceptionMsg = null; // 全局异常

	public WordWriter(String[] headerNames, String[] fieldNames) {
		this.headerNames = headerNames;
		this.fieldNames = fieldNames;
	}

	/**
	 * 导入
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public boolean writer(String fileName, List<T> data, HttpServletResponse response) throws Exception {
		try {		
			// 数据集
			this.data = data;
			// 设置请求
			response.setContentType("application/application/vnd.ms-excel");
			response.setHeader("Content-disposition","attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			// 创建Word文档
			document = new XWPFDocument();
			// 添加大标题 
			String title = fileName.substring(0, fileName.indexOf("."));
			XWPFParagraph titleParagraph = document.createParagraph();  
			titleParagraph.setAlignment(ParagraphAlignment.CENTER); // 设置段落居中
			XWPFRun titleParagraphRun = titleParagraph.createRun();  
			titleParagraphRun.setText(title);
			titleParagraphRun.setColor("000000");  
			titleParagraphRun.setFontSize(20); 
			// 添加表格
			createTable();
 			return true;
		} catch (Exception e) {
			logger.error("public boolean writer(List<T> data, String fileName, HttpServletResponse response)");
            logger.error("导出Word时: 导出Word出错!");
            e.printStackTrace();
            return false;
		} finally {
			OutputStream out  = response.getOutputStream();
            // 写入输出流
            document.write(out);
            // 关闭输出流
            out.close();
		}
	}
	
	/**
	 * 创建表格
	 * @throws Exception
	 */
	private void createTable() throws Exception {
		// 创建表格
		XWPFTable table = document.createTable();
		// 列宽自动分割  
        CTTblWidth comTableWidth = table.getCTTbl().addNewTblPr().addNewTblW();  
        comTableWidth.setType(STTblWidth.DXA);  
        comTableWidth.setW(BigInteger.valueOf(9072));
        // 添加列名称行
        XWPFTableRow tableRow = table.createRow();
        for (int i = 0, len = headerNames.length; i < len; i++) {
        	if(i == 0) {
        		tableRow.getCell(0).setText(headerNames[0]);
        	}else {        		
        		tableRow.addNewTableCell().setText(headerNames[i]);
        	}
		}
        // 添加数据行
        writerData(table);
        // 删除空白的第一行
        table.removeRow(0);
	}
	
	/**
	 * 写入数据到表格
	 * @param table
	 * @throws Exception
	 */
	private void writerData(XWPFTable table) throws Exception {
		// 临时变量
		Class<? extends Object> tCls = null; // 当前对象的class  
		String[] fieldNamesChild = null; // 主要为了处理"user.office.officeName"复杂属性
		String fieldName = null; // 当前属性名称，可以是一级属性user，也可以是二三级属性 office.officeName
		Method getMethod = null; // 当前一级属性的get方法
		String[] change = null; // 类型转换（例如：1:男;2:女）
		int len = -1; // '|'字符的下标
		XWPFTableRow tableRow = null; XWPFTableCell cell = null;
		// 循环添加数据
		for (int i = 0, len1 = this.data.size(); i < len1; i++) {
			// 获取当前对象
			T t = (T) this.data.get(i);
			// 创建当前行
			tableRow = table.createRow();
			// 本次数据导出的主对象的class,只在第一次调用时赋值
			if(tCls == null)tCls = t.getClass();
			for (short j = 0; j < this.fieldNames.length; j++) {
				if(j == 0) {
					cell = tableRow.getCell(0); 	
				}else {
					cell = tableRow.addNewTableCell();
				}
				
				len = this.fieldNames[j].indexOf("|"); // |字符的下标
				
				// 是否含需要转换类型的参数
				if(len!=-1) {
					// 类型转换（例如：1:男;2:女）
					change = this.fieldNames[j].substring(len+1).split(";");
					// 获取属性名数组（针对复杂对象，否则可以不必转为数组）
					fieldNamesChild = this.fieldNames[j].substring(0, len).split("\\.");
				}else {
					change = null;
					// 获取属性名数组（针对复杂对象，否则可以不必转为数组）
					fieldNamesChild = this.fieldNames[j].split("\\.");
				}
				// 获取属性名（如id、name、org.id）
				fieldName = fieldNamesChild[0];
				// 获取一级属性的get方法
				getMethod = BeanUtil.getGetMethodByFieldName(tCls, fieldName);
				// 获取一级属性的值
				Object value = getMethod.invoke(t, new Object[] {});
				// 获取多级对象的属性值，一般用得到，当对象属性为复杂对象（user.office.officeName）时，循环获取多级属性的最终值(officeName)
				for (int k = 1, len3 = fieldNamesChild.length; k < len3; k++) {
					fieldName = fieldNamesChild[k];
					value = getValueByKey(value, fieldName);
				}
				// 判断值的类型后进行强制类型转换
				typeRotation(cell, value, change);
			}
		}
	}
	
	/**
     * 通过对象和属性名获取属性值
     * @param obj
     * @param key
     * @return
     * @throws Exception 
     */
    private Object getValueByKey(Object obj, String key) throws Exception {
    	Class<?> clazz = obj.getClass();
    	// 重组正确的Class（过滤类名后带着的类_$$_jvstfaa_1字符串）
    	String clazzStr = clazz.toString();
    	int _len = clazzStr.indexOf("_");
    	if(_len>0) {
    		clazz = Class.forName(clazzStr.substring(0, _len).replace("class ", ""));    		    		
    	}
    	// 获得属性的get方法
    	Method method = BeanUtil.getGetMethodByFieldName(clazz, key);
        // 拿到get方法上的时间格式化注解,时间类型时有用
		DateTimeFormat dateTimeFormat = method.getAnnotation(DateTimeFormat.class);
		if(dateTimeFormat == null || StringUtils.isBlank(dateTimeFormat.pattern())) {
			this.format = "yyyy-MM-dd";
		}else {
			this.format = dateTimeFormat.pattern();
		}
        Object value = method.invoke(obj, new Object[] {});    
        return value;
    }
    
    /**
     * 判断值的类型后进行强制类型转换
     * @param cell
     * @param value
     * @param change 字符转换 1:男;2:女
     */
    private void typeRotation(XWPFTableCell cell, Object value, String[] change) {
        try {           
            String textValue = null;
            if (value instanceof Date) {
                Date date = (Date) value;
                SimpleDateFormat sdf = new SimpleDateFormat(this.format);
                textValue = sdf.format(date);
            } else {
                // 其它数据类型都当作字符串简单处理
                if (value == null) {
                    value = "";
                }
                textValue = value.toString();
            }
            if (textValue != null) {
                Pattern p = Pattern.compile("^//d+(//.//d+)?{1}quot;");
                Matcher matcher = p.matcher(textValue);
                if (matcher.matches()) {
                    // 是数字当作double处理
                    cell.setText(Double.parseDouble(textValue) + "");
                } else {
                	// 字符串
                	if(change != null) {
                		String[] temp = null; boolean flag = false;
                		for (int i = 0; i < change.length; i++) {
                			temp = change[i].split(":");
							if(temp.length > 0 && temp[0].equals(textValue)) {								
								// 字符转换处理（1:男;2:女）
								cell.setText(temp[1]);
								flag = true;
								break;
							}
						}
                		if(!flag) { cell.setText(textValue); }
                	}else {                		
                		cell.setText(textValue);
                	}
                }
            }
        } catch (Exception e) {
            logger.error("private void typeRotation(Cell cell, Object value)");
            logger.error("导出Word时: 判断值的类型后进行强制类型转换时出错!");
            exceptionMsg = "导出Word时: 判断值的类型后进行强制类型转换时出错!";
            e.printStackTrace();
        }
    }
}