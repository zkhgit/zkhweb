package zkh.tool.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import zkh.tool.excel.logic.ExcelReader;

/**
 * 导入工具
 *
 * 赵凯浩
 * 2018年11月15日 上午10:13:57
 */
public class ExcelImport {

	// 错误信息集合
	public static List<String> errorList = null;
	
	/**
	 * 导入
	 * @param excelPath Excel文件路径
	 * @param xmlPath Xml文件路径
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public static <T extends Object> List<T> importData(String excelPath, String xmlPath, String elementName) throws Exception {
		// 导入开始前新建errorList
		errorList = new ArrayList<String>();
		FileInputStream fis = new FileInputStream(new File(excelPath));
		Workbook workbook = zkh.tool.excel.logic.Workbook.reader(fis, excelPath);
		// 获取Excel数据list
		ExcelReader excelReader = new ExcelReader();
		List<T> list = excelReader.readExcel(excelPath, xmlPath, elementName, workbook);
		errorList = excelReader.errorList;
		// 释放资源
    	workbook.close();
    	fis.close();
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
	public static <T extends Object> List<T> importDataBigData(String excelPath, String xmlPath, String elementName) throws Exception {
		// 导入开始前新建errorList
		errorList = new ArrayList<String>();
		FileInputStream fis = new FileInputStream(new File(excelPath));
		Workbook workbook = zkh.tool.excel.logic.Workbook.readerBigData(fis, excelPath);
		// 获取Excel数据list
		ExcelReader excelReader = new ExcelReader();
		List<T> list = excelReader.readExcel(excelPath, xmlPath, elementName, workbook);
		// 释放资源
    	workbook.close();
    	fis.close();
    	return list;
	}
	
}
