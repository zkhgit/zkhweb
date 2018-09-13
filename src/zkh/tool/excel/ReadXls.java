package zkh.tool.excel;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import zkh.tool.excel.util.CellUtil;

/**
 * 2003.xls文件导入
 * 描述：POI使用HSSF开发的接口api
 * 赵凯浩
 * 2018年8月15日 下午4:25:38
 */
public class ReadXls {

	public static void testRead() throws Exception{
        // 创建输入流
        FileInputStream fis = new FileInputStream(new File("G:\\电勘院2015年新办人员25人20150409.xls"));
        // 通过构造函数传参
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        // 获取工作表
        HSSFSheet sheet = workbook.getSheetAt(0);
        
        for (Row row : sheet) {
            for (Cell cell : row) {
            	CellUtil.getValueByCell(cell);
            	System.out.print(CellUtil.getValueByCell(cell)+" ");
            }
            System.out.println();
        }
        
        workbook.close();
        fis.close();
    }
	
	public static void main(String[] args) {
		try {
			testRead();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
