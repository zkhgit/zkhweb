package zkh.tool.excel.common;

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

	/**
	 * 获取Workbook
	 * @param path
	 * @return
	 */
	public static org.apache.poi.ss.usermodel.Workbook create(String path) {
		org.apache.poi.ss.usermodel.Workbook workbook = null; 
		
		if (path.endsWith(".xls")) {
			workbook = new HSSFWorkbook();
        } else if (path.endsWith(".xlsx")) {
        	workbook = new XSSFWorkbook();
        } else {
            throw new RuntimeException("文件出错，非excel文件");
        }
		
		return workbook;
	}
	
	/**
	 * 获取Workbook
	 * 描述：用于大数据量
	 * @param path
	 * @param openBigData
	 * @return
	 */
	public static org.apache.poi.ss.usermodel.Workbook createBigData() throws Exception{
		return new XSSFWorkbook();
	}
	
	/**
	 * 获取Workbook
	 * @param path
	 * @param openBigData
	 * @return
	 */
	public static org.apache.poi.ss.usermodel.Workbook create(String path, boolean openBigData) {
		org.apache.poi.ss.usermodel.Workbook workbook = null; 
		
		if(!!openBigData) {
			workbook = new SXSSFWorkbook(100);
		}else if (path.endsWith(".xls")) {
			workbook = new HSSFWorkbook();
        } else if (path.endsWith(".xlsx")) {
        	workbook = new XSSFWorkbook();
        } else {
            throw new RuntimeException("文件出错，非excel文件");
        }
		
		return workbook;
	}
    
    private Workbook() {}
}
