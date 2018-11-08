package zkh.tool.excel;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zkh.bean.DataResult;
import zkh.tool.excel.logic.Excel2003Writer;
import zkh.tool.excel.logic.Excel2007BigDataWriter;
import zkh.tool.excel.logic.Excel2007Writer;
import zkh.tool.excel.logic.ExcelWriter;

/**
 * Excel导出工具
 *
 * 赵凯浩
 * 2018年11月8日 下午2:10:45
 */
public class ExcelWriterUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(ExcelWriterUtil.class);
	
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
	public static <T> DataResult download(List<T> data, String [] headerNames, String [] fieldNames, String fileName, String format, int pageSize, HttpServletResponse response) {
		DataResult result = new DataResult();
		
		// 计时器
    	StopWatch stopWatch = new StopWatch();
    	// 计时开始
    	stopWatch.start();
    	
		try {
			ExcelWriter writer = null;
			String suffix = getSuffix(fileName);
			if(StringUtils.isBlank(suffix)) {
				
			}else if(suffix.equals("xls")) {
				writer = new Excel2003Writer();
				result = writer.writer(data, headerNames, fieldNames, fileName, format, pageSize, response);
			}else {
				writer = new Excel2007Writer();
				result = writer.writer(data, headerNames, fieldNames, fileName, format, pageSize, response);
			}
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			// 计时结束
            stopWatch.stop();
		}
		
		result.setMsg(stopWatch.getTime() + "");
    	logger.info("Excel导出完毕，用时" + stopWatch.getTime() + "毫秒");
		return result;
	}
	
	/**
	 * 参数简化版
	 * @param data
	 * @param headerNames
	 * @param fieldNames
	 * @param fileName
	 * @param response
	 * @return
	 */
	public static <T> DataResult downloadSimple(List<T> data, String [] headerNames, String [] fieldNames, String fileName, HttpServletResponse response) {
		return download(data, headerNames, fieldNames, fileName, null, 10000, response);
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
	public static <T> DataResult downloadBigData(List<T> data, String [] headerNames, String [] fieldNames, String fileName, String format, int pageSize, HttpServletResponse response) {
		ExcelWriter writer = new Excel2007BigDataWriter();
		DataResult result = new DataResult();
		
		// 计时器
    	StopWatch stopWatch = new StopWatch();
    	// 计时开始
    	stopWatch.start();
    	
		try {
			result = writer.writer(data, headerNames, fieldNames, fileName, format, pageSize, response);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			// 计时结束
            stopWatch.stop();
		}
		
		result.setMsg(stopWatch.getTime() + "");
    	logger.info("Excel导出完毕，用时" + stopWatch.getTime() + "毫秒");
		return result;
	}
	
	/**
	 * 获取文件后缀名
	 * @param fileName
	 * @return
	 */
	private static String getSuffix(String fileName) {
		if(fileName == null) return null;
		int index = fileName.lastIndexOf(".");
		String suffix = fileName.substring(index+1);
		return suffix;
	}
	
	private ExcelWriterUtil() {};
	
}
