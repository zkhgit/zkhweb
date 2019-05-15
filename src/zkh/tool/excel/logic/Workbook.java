package zkh.tool.excel.logic;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取Workbook
 *
 * 赵凯浩
 * 2018年11月9日 下午5:14:59
 */
public class Workbook {
	
	private static Logger logger = LoggerFactory.getLogger(Workbook.class);

	// 导入**********************************************************************/
	/**
	 * 获取Workbook
	 * @param excelPath
	 * @return
	 */
	public static org.apache.poi.ss.usermodel.Workbook reader(InputStream fis, String excelPath) {
		org.apache.poi.ss.usermodel.Workbook workbook = null;     
		try {			
			if (excelPath.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			} else if (excelPath.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}
		} catch (Exception e) {
			logger.error("public static org.apache.poi.ss.usermodel.Workbook reader(FileInputStream fis, String excelPath)");
			logger.error("导出Excel时出错: 获取Workbook出错!");
		}
		return workbook;
	}
	
	/**
	 * 获取Workbook
	 * 描述：用于大数据量
	 * @param excelPath
	 * @return
	 * @throws Exception
	 */
	public static org.apache.poi.ss.usermodel.Workbook readerBigData(FileInputStream fis) {
		org.apache.poi.ss.usermodel.Workbook workbook = null;
		try {
			workbook = new SXSSFWorkbook(new XSSFWorkbook(fis), 100);
		} catch (Exception e) {
			logger.error("public static org.apache.poi.ss.usermodel.Workbook readerBigData(FileInputStream fis, String excelPath)");
			logger.error("导出Excel时出错: 获取Workbook出错!");
		}
		return workbook;
	}
	
	// 导出*************************************************************************************/
	/**
	 * 获取Workbook
	 * @param excelPath
	 * @return
	 */
	public static org.apache.poi.ss.usermodel.Workbook writer(String excelPath) {
		org.apache.poi.ss.usermodel.Workbook workbook = null;     
		try {			
			if (excelPath.endsWith(".xls")) {
				workbook = new HSSFWorkbook();
			} else if (excelPath.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook();
			}
		} catch (Exception e) {
			logger.error("public static org.apache.poi.ss.usermodel.Workbook writer(String excelPath)");
			logger.error("导出Excel时出错: 获取Workbook出错!");
		}
		return workbook;
	}
	
	/**
	 * 获取Workbook
	 * 描述：用于大数据量
	 * @param excelPath
	 * @return
	 * @throws Exception
	 */
	public static org.apache.poi.ss.usermodel.Workbook writerBigData(String excelPath) {
		org.apache.poi.ss.usermodel.Workbook workbook = null;
		try {			
			workbook = new SXSSFWorkbook(new XSSFWorkbook(), 100);
		} catch (Exception e) {
			logger.error("public static org.apache.poi.ss.usermodel.Workbook writerBigData(String excelPath)");
			logger.error("导出Excel时出错: 获取Workbook出错!");
		}
		return workbook;
	}
    
    private Workbook() {}
    
}
