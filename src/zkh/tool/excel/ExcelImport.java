package zkh.tool.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import zkh.tool.excel.logic.ExcelReader;

/**
 * 导入工具
 *
 * 赵凯浩
 * 2018年11月15日 上午10:13:57
 * @param <T>
 */
public class ExcelImport<T> {
	
	private Logger logger = LoggerFactory.getLogger(ExcelImport.class);

	// 导入的数据
	public List<T> dataList = null;
	// 数据错误信息集合
	public List<String> errorList = null;
	// 异常提示信息
	public String exceptionMsg = null;

	/**
	 * 快捷导入
	 * @param excelImport 存放导入数据、验证信息
	 * @param file 页面传过来的form表单中的file
	 * @param xmlPath xml模板文件相对与resources的路径
	 * @param mobalName xml模板中指定的模型名称
	 * @return
	 * @throws Exception
	 */
	public ExcelImport<T> importDataSimple(MultipartFile file, String xmlPath, String mobalName) throws Exception{
		File excelFile = null;
		// 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        excelFile = File.createTempFile(UUID.randomUUID() + "", prefix);
        // MultipartFile to File
        file.transferTo(excelFile);
        // 获取Excel导入模板文件
		Resource resource = new ClassPathResource(xmlPath);
		// 获取要导入数据库的数据集
		importData(excelFile, fileName, resource.getFile(), mobalName);
		return this;
	}
	
	/**
	 * 导入
	 * @param excelPath Excel文件路径
	 * @param xmlPath Xml文件路径
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public List<T> importData(String excelPath, File xmlFile, String elementName) {
		try {			
			// 导入开始前新建errorList
			errorList = new ArrayList<String>();
			InputStream fis = new FileInputStream(new File(excelPath));
			Workbook workbook = zkh.tool.excel.logic.Workbook.reader(fis, excelPath);
			// 获取Excel数据list
			ExcelReader excelReader = new ExcelReader();
			dataList = excelReader.readExcel(xmlFile, elementName, workbook);
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
    	return dataList;
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
	public List<T> importData(File file, String fileName, File xmlFile, String elementName) {
		try {			
			// 导入开始前新建errorList
			errorList = new ArrayList<String>();
			InputStream fis = new FileInputStream(file);
			Workbook workbook = zkh.tool.excel.logic.Workbook.reader(fis, fileName);
			// 获取Excel数据list
			ExcelReader excelReader = new ExcelReader();
			dataList = excelReader.readExcel(xmlFile, elementName, workbook);
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
    	return dataList;
	}
	
	/**
	 * 超大数据量快捷导入
	 * @param excelPath Excel文件路径
	 * @param xmlPath Xml文件路径
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public ExcelImport<T> importBigDataSimple(ExcelImport<T> excelImport, MultipartFile file, String xmlPath, String mobalName) throws Exception{
		File excelFile = null;
		// 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        excelFile = File.createTempFile(UUID.randomUUID() + "", prefix);
        // MultipartFile to File
        file.transferTo(excelFile);
        // 获取Excel导入模板文件
		Resource resource = new ClassPathResource(xmlPath);
		// 获取要导入数据库的数据集
		excelImport.importBigData(excelFile, fileName, resource.getFile(), mobalName);
		return excelImport;
	}
	
	/**
	 * 超大数据量的导入
	 * @param excelPath Excel文件路径
	 * @param xmlPath Xml文件路径
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public List<T> importBigData(String excelPath, File xmlFile, String elementName) {
		try {			
			// 导入开始前新建errorList
			errorList = new ArrayList<String>();
			FileInputStream fis = new FileInputStream(new File(excelPath));
			Workbook workbook = zkh.tool.excel.logic.Workbook.readerBigData(fis);
			// 获取Excel数据list
			ExcelReader excelReader = new ExcelReader();
			dataList = excelReader.readExcel(xmlFile, elementName, workbook);
			// 释放资源
			workbook.close();
			fis.close();
		} catch (Exception e) {
			logger.error("public static <T extends Object> List<T> importDataBigData(String excelPath, String xmlPath, String elementName)");
			logger.error("导入Excel时: 调用超大数据量的导入出错!");
			exceptionMsg = "导入Excel时: 调用超大数据量的导入出错!";
			e.printStackTrace();
		}
    	return dataList;
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
	public List<T> importBigData(File file, String fileName, File xmlFile, String elementName) {
		try {			
			// 导入开始前新建errorList
			errorList = new ArrayList<String>();
			FileInputStream fis = new FileInputStream(file);
			Workbook workbook = zkh.tool.excel.logic.Workbook.readerBigData(fis);
			// 获取Excel数据list
			ExcelReader excelReader = new ExcelReader();
			dataList = excelReader.readExcel(xmlFile, elementName, workbook);
			// 释放资源
			workbook.close();
			fis.close();
		} catch (Exception e) {
			logger.error("public static <T extends Object> List<T> importDataBigData(String excelPath, String xmlPath, String elementName)");
			logger.error("导入Excel时: 调用超大数据量的导入出错!");
			exceptionMsg = "导入Excel时: 调用超大数据量的导入出错!";
			e.printStackTrace();
		}
    	return dataList;
	}

	/**
	 * 删除文件
	 * @param files
	 */
	public void deleteFile(File... files) {  
		for (File file : files) {  
			if (file.exists()) {  
				file.delete();  
			}  
		}  
	}
	
}