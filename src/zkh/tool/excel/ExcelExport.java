package zkh.tool.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zkh.tool.excel.logic.ExcelStyle;
import zkh.tool.excel.logic.ExcelWriter;

/**
 * Excel导出工具
 *
 * 赵凯浩
 * 2018年11月8日 下午2:10:45
 */
public class ExcelExport<T> {
	
	private static Logger logger = LoggerFactory.getLogger(ExcelExport.class);
	
	// 导出时文件名称
	private String fileName;
	private HttpServletResponse response;
	
	// 异常提示信息
	public String exceptionMsg = null;
	
	public ExcelExport(String fileName, HttpServletResponse response) {
		// 这些参数跟导入的数据无关
		this.fileName = fileName;
		this.response = response;
	}
	
	/**
	 * Excel导出
	 * 描述：参数简化版
	 * @param title sheet第一行的大标题
	 * @param data 数据list
	 * @param headerNames 标题名称数组
	 * @param fieldNames 数据字段名称数组
	 * @return
	 * @throws Exception
	 */
	public boolean export(String title, List<T> data, String [] headerNames, String [] fieldNames){
		Workbook workbook = zkh.tool.excel.logic.Workbook.writer(this.fileName);
        return exportBase(title, data, headerNames, fieldNames, null, 0, workbook, null);
	}
	
	/**
	 * Excel导出
	 * 描述：参数简化版+时间格式化
	 * @param title sheet第一行的大标题
	 * @param data 数据list
	 * @param headerNames 标题名称数组
	 * @param fieldNames 数据字段名称数组
	 * @param format 时间格式化
	 * @return
	 * @throws Exception
	 */
	public boolean export(String title, List<T> data, String [] headerNames, String [] fieldNames, String format){
		Workbook workbook = zkh.tool.excel.logic.Workbook.writer(this.fileName);
        return exportBase(title, data, headerNames, fieldNames, format, 0, workbook, null);
	}
	
	/**
	 * Excel导出
	 * 描述：全参数版
	 * @param title sheet第一行的大标题
	 * @param data 数据list
	 * @param headerNames 标题名称数组
	 * @param fieldNames 数据字段名称数组
	 * @param format 时间格式化
	 * @param pageSize 每个sheet子页数据条数
	 * @param excelStyle 自定义Excel样式
	 * @return
	 * @throws Exception
	 */
	public boolean export(String title, List<T> data, String [] headerNames, String [] fieldNames, String format, int pageSize, ExcelStyle excelStyle) throws Exception{
		Workbook workbook = zkh.tool.excel.logic.Workbook.writer(fileName);
        return exportBase(title, data, headerNames, fieldNames, format, pageSize, workbook, excelStyle);
	}

	/**
	 * Excel超大数据量的导出
	 * 描述：参数简化版
	 * @param title sheet第一行的大标题
	 * @param data 数据list
	 * @param headerNames 标题名称数组
	 * @param fieldNames 数据字段名称数组
	 * @return
	 * @throws Exception
	 */
	public boolean exportBigData(String title, List<T> data, String [] headerNames, String [] fieldNames) throws Exception{
		Workbook workbook = zkh.tool.excel.logic.Workbook.writerBigData(this.fileName);
		return exportBase(title, data, headerNames, fieldNames, null, 0, workbook, null);
	}
	
	/**
	 * Excel超大数据量的导出
	 * 描述：参数简化版+时间格式化
	 * @param title sheet第一行的大标题
	 * @param data 数据list
	 * @param headerNames 标题名称数组
	 * @param fieldNames 数据字段名称数组
	 * @param format 时间格式化
	 * @return
	 * @throws Exception
	 */
	public boolean exportBigData(String title, List<T> data, String [] headerNames, String [] fieldNames, String format) throws Exception{
		Workbook workbook = zkh.tool.excel.logic.Workbook.writerBigData(this.fileName);
		return exportBase(title, data, headerNames, fieldNames, format, 0, workbook, null);
	}
	
	/**
	 * Excel超大数据量的导出
	 * 描述：全参数版
	 * @param title sheet第一行的大标题
	 * @param data 数据list
	 * @param headerNames 标题名称数组
	 * @param fieldNames 数据字段名称数组
	 * @param format 时间格式化
	 * @param pageSize 每个sheet子页数据条数
	 * @param excelStyle 自定义Excel样式
	 * @return
	 * @throws Exception
	 */
	public boolean exportBigData(String title, List<T> data, String [] headerNames, String [] fieldNames, String format, int pageSize, ExcelStyle excelStyle){
		Workbook workbook = zkh.tool.excel.logic.Workbook.writerBigData(this.fileName);
		return exportBase(title, data, headerNames, fieldNames, format, pageSize, workbook, excelStyle);
	}
	
	// 基础接口
	private boolean exportBase(String title, List<T> data, String [] headerNames, String [] fieldNames, String format, int pageSize, Workbook workbook, ExcelStyle excelStyle){
		boolean flag = true;
		try {			
			Map<String, CellStyle> customStyles = null;
			if(excelStyle!=null) {				
				customStyles = new HashMap<String, CellStyle>();
				customStyles.put("title", excelStyle.getTitle());
				customStyles.put("header", excelStyle.getHeader());
				customStyles.put("data", excelStyle.getData());
			}
			// 获取输出流
			ExcelWriter<T> writer = new ExcelWriter<>(title, headerNames, fieldNames, format, workbook, customStyles);
			flag = writer.writer(data, this.fileName, pageSize, this.response);
			writer.exceptionMsg = exceptionMsg;
		} catch (Exception e) {
			logger.error("private boolean exportBase(String title, List<T> data, String [] headerNames, String [] fieldNames, String format, int pageSize, Workbook workbook, Map<String, CellStyle> customStyles)");
			logger.error("导出Excel时: 调用基础接口出错!");
			exceptionMsg = "导出Excel时: 调用基础接口出错!";
			e.printStackTrace();
		}
        return flag;
	}

}
