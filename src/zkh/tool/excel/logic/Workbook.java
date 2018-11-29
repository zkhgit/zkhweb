package zkh.tool.excel.logic;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 获取Workbook
 *
 * 赵凯浩
 * 2018年11月9日 下午5:14:59
 */
public class Workbook {

	// 导入**********************************************************************/
	/**
	 * 获取Workbook
	 * @param excelPath
	 * @return
	 */
	public static org.apache.poi.ss.usermodel.Workbook reader(FileInputStream fis, String excelPath) throws Exception{
		org.apache.poi.ss.usermodel.Workbook workbook = null;     
		if (excelPath.endsWith(".xls")) {
			workbook = new HSSFWorkbook(fis);
        } else if (excelPath.endsWith(".xlsx")) {
        	workbook = new XSSFWorkbook(fis);
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
	public static org.apache.poi.ss.usermodel.Workbook readerBigData(FileInputStream fis, String excelPath) throws Exception{
		return new SXSSFWorkbook(new XSSFWorkbook(fis), 100);
	}
	
	/**
	 * 获取Workbook
	 * @param excelPath
	 * @param openBigData
	 * @return
	 */
	public static org.apache.poi.ss.usermodel.Workbook reader(FileInputStream fis, String excelPath, boolean openBigData) throws Exception{
		org.apache.poi.ss.usermodel.Workbook workbook = null;
		if(!!openBigData) {
			workbook = new SXSSFWorkbook(new XSSFWorkbook(fis), 100);
		}else if (excelPath.endsWith(".xls")) {
			workbook = new HSSFWorkbook(fis);
        } else if (excelPath.endsWith(".xlsx")) {
        	workbook = new XSSFWorkbook(fis);
        }
		return workbook;
	}
	
	
	// 导出*************************************************************************************/
	/**
	 * 获取Workbook
	 * @param excelPath
	 * @return
	 */
	public static org.apache.poi.ss.usermodel.Workbook writer(String excelPath) throws Exception{
		org.apache.poi.ss.usermodel.Workbook workbook = null;     
		if (excelPath.endsWith(".xls")) {
			workbook = new HSSFWorkbook();
        } else if (excelPath.endsWith(".xlsx")) {
        	workbook = new XSSFWorkbook();
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
	public static org.apache.poi.ss.usermodel.Workbook writerBigData(String excelPath) throws Exception{
		return new SXSSFWorkbook(new XSSFWorkbook(), 100);
	}
	
	/**
	 * 获取Workbook
	 * @param excelPath
	 * @param openBigData
	 * @return
	 */
	public static org.apache.poi.ss.usermodel.Workbook writer(String excelPath, boolean openBigData) throws Exception{
		org.apache.poi.ss.usermodel.Workbook workbook = null;
		if(!!openBigData) {
			workbook = new SXSSFWorkbook(new XSSFWorkbook(), 100);
		}else if (excelPath.endsWith(".xls")) {
			workbook = new HSSFWorkbook();
        } else if (excelPath.endsWith(".xlsx")) {
        	workbook = new XSSFWorkbook();
        }
		return workbook;
	}
    
    private Workbook() {}
    
}
