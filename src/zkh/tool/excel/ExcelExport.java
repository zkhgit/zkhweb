package zkh.tool.excel;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import zkh.bean.DataResult;
import zkh.tool.excel.logic.ExcelWriter;

/**
 * Excel导出工具
 *
 * 赵凯浩
 * 2018年11月8日 下午2:10:45
 */
public class ExcelExport {
	
	/**
	 * Excel导出
	 * @param data 数据list
	 * @param headerNames 标题名称数组
	 * @param fieldNames 数据字段名称数组 
	 * @param fileName 导出时文件名名称
	 * @param format 时间格式化
	 * @param pageSize 每个sheet子页数据条数
	 * @param response
	 * @return
	 */
	public static <T> DataResult export(List<T> data, String [] headerNames, String [] fieldNames, String fileName, String format, int pageSize, HttpServletResponse response) throws Exception{
		Workbook workbook = zkh.tool.excel.logic.Workbook.writer(fileName);
        return exportBase(data, headerNames, fieldNames, fileName, format, pageSize, workbook, response);
	}
	
	/**
	 * 超大数据量的导出
	 * @param data
	 * @param headerNames
	 * @param fieldNames
	 * @param fileName
	 * @param format
	 * @param pageSize
	 * @param response
	 * @return
	 */
	public static <T> DataResult exportBigData(List<T> data, String [] headerNames, String [] fieldNames, String fileName, String format, int pageSize, HttpServletResponse response) throws Exception{
		Workbook workbook = zkh.tool.excel.logic.Workbook.writerBigData(fileName);
		return exportBase(data, headerNames, fieldNames, fileName, format, pageSize, workbook, response);
	}
	
	private static <T> DataResult exportBase(List<T> data, String [] headerNames, String [] fieldNames, String fileName, String format, int pageSize, Workbook workbook, HttpServletResponse response) throws Exception{
		// 获取输出流
		ExcelWriter writer = new ExcelWriter();
		DataResult dataResult = writer.writer(data, headerNames, fieldNames, fileName, format, pageSize, workbook, response);
        return dataResult;
	}
	
	private ExcelExport() {};
	
}
