package zkh.tool.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zkh.tool.excel.logic.ExcelReader;


/**
 * 导入工具
 *
 * 赵凯浩
 * 2018年11月15日 上午10:13:57
 */
public class ExcelImport {
	
	private Logger logger = LoggerFactory.getLogger(ExcelImport.class);

	// 数据错误信息集合
	public List<String> errorList = null;
	// 异常提示信息
	public String exceptionMsg = null;

	/**
	 * 导入
	 * @param excelPath Excel文件路径
	 * @param xmlPath Xml文件路径
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> importData(String excelPath, String xmlPath, String elementName) {
		List<T> list = null;
		try {			
			// 导入开始前新建errorList
			errorList = new ArrayList<String>();
			InputStream fis = new FileInputStream(new File(excelPath));
			Workbook workbook = zkh.tool.excel.logic.Workbook.reader(fis, excelPath);
			// 获取Excel数据list
			ExcelReader excelReader = new ExcelReader();
			list = excelReader.readExcel(xmlPath, elementName, workbook);
			errorList = excelReader.errorList;
			exceptionMsg = excelReader.exceptionMsg;
			// 释放资源
			workbook.close();
			fis.close();
		} catch (Exception e) {
			logger.error("public static <T extends Object> List<T> importData(String excelPath, String xmlPath, String elementName)");
			logger.error("导入Excel时: 导入出错!");
			exceptionMsg = "导入Excel时: 导入出错!";
			e.printStackTrace();
		}
    	return list;
	}
	
	/**
	 * 导入
	 * @param file Excel文件
	 * @param fileName Excel文件名
	 * @param xmlPath Xml文件路径
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> importData(File file, String fileName, String xmlPath, String elementName) {
		List<T> list = null;
		try {			
			// 导入开始前新建errorList
			errorList = new ArrayList<String>();
			InputStream fis = new FileInputStream(file);
			Workbook workbook = zkh.tool.excel.logic.Workbook.reader(fis, fileName);
			// 获取Excel数据list
			ExcelReader excelReader = new ExcelReader();
			list = excelReader.readExcel(xmlPath, elementName, workbook);
			errorList = excelReader.errorList;
			// 释放资源
			workbook.close();
			fis.close();
		} catch (Exception e) {
			logger.error("public static <T extends Object> List<T> importData(String excelPath, String xmlPath, String elementName)");
			logger.error("导入Excel时: 导入出错!");
			exceptionMsg = "导入Excel时: 导入出错!";
			e.printStackTrace();
		}
    	return list;
	}
	
	/**
	 * 超大数据量的导入
	 * @param excelPath Excel文件路径
	 * @param xmlPath Xml文件路径
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> importBigData(String excelPath, String xmlPath, String elementName) {
		List<T> list = null;
		try {			
			// 导入开始前新建errorList
			errorList = new ArrayList<String>();
			FileInputStream fis = new FileInputStream(new File(excelPath));
			Workbook workbook = zkh.tool.excel.logic.Workbook.readerBigData(fis, excelPath);
			// 获取Excel数据list
			ExcelReader excelReader = new ExcelReader();
			list = excelReader.readExcel(xmlPath, elementName, workbook);
			// 释放资源
			workbook.close();
			fis.close();
		} catch (Exception e) {
			logger.error("public static <T extends Object> List<T> importDataBigData(String excelPath, String xmlPath, String elementName)");
			logger.error("导入Excel时: 调用超大数据量的导入出错!");
			exceptionMsg = "导入Excel时: 调用超大数据量的导入出错!";
			e.printStackTrace();
		}
    	return list;
	}
	
	/**
	 * 超大数据量的导入
	 * @param file Excel文件
	 * @param fileName Excel文件名
	 * @param xmlPath Xml文件路径
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> importBigData(File file, String fileName, String xmlPath, String elementName) {
		List<T> list = null;
		try {			
			// 导入开始前新建errorList
			errorList = new ArrayList<String>();
			FileInputStream fis = new FileInputStream(file);
			Workbook workbook = zkh.tool.excel.logic.Workbook.readerBigData(fis, fileName);
			// 获取Excel数据list
			ExcelReader excelReader = new ExcelReader();
			list = excelReader.readExcel(xmlPath, elementName, workbook);
			// 释放资源
			workbook.close();
			fis.close();
		} catch (Exception e) {
			logger.error("public static <T extends Object> List<T> importDataBigData(String excelPath, String xmlPath, String elementName)");
			logger.error("导入Excel时: 调用超大数据量的导入出错!");
			exceptionMsg = "导入Excel时: 调用超大数据量的导入出错!";
			e.printStackTrace();
		}
    	return list;
	}
	
}
