package zkh.tool.word;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zkh.tool.word.logic.WordWriter;
import zkh.tool.word.logic.WordWriterByXml;

/**
 * Word导出工具
 *
 * 赵凯浩
 * 2019年1月22日 下午3:07:49
 */
public class WordExport<T> {
	
	private static Logger logger = LoggerFactory.getLogger(WordExport.class);
	
	// 导出时文件名称
	private String fileName; // 文件名（含后缀）
	private HttpServletResponse response;
	private String [] headers; // 属性名称 
	private String [] names; // 属性
	
	// 异常提示信息
	public String exceptionMsg = null;
	
	public WordExport() {}
	
	public WordExport(String fileName, HttpServletRequest request, HttpServletResponse response) {
		this.fileName = fileName;
		this.response = response;
		this.headers = ((String)request.getParameter("headers")).split(",");
		this.names = ((String)request.getParameter("names")).split(",");
	}
	
	public boolean export(List<T> data) throws Exception {
		return exportBase(data);
	}
	
	/**
	 * 导出Word
	 * 描述：通过Word生成的Xml模板导出
	 * @param fileName 导出的word文件的文件名（含后缀，后缀由xml模板决定）
	 * @param xmlPath xml模板文件的路径
	 * @param map 数据
	 * @param response
	 * @return
	 */
	public void exportByXml(String fileName, String xmlPath, Map<String, Object> map, HttpServletResponse response) throws Exception {
		new WordWriterByXml().export(fileName, xmlPath, map, response);
	}
	
	// 基础接口
	private boolean exportBase(List<T> data){
		boolean flag = true;
		try {			
			// 获取输出流
			WordWriter<T> writer = new WordWriter<T>(this.headers, this.names);
			flag = writer.writer(this.fileName, data, this.response);
			writer.exceptionMsg = exceptionMsg;
		} catch (Exception e) {
			logger.error("private boolean exportBase2007(List<T> data)");
			logger.error("导出Word时: 调用基础接口出错!");
			exceptionMsg = "导出Word时: 调用基础接口出错!";
			e.printStackTrace();
		}
        return flag;
	}
	
}