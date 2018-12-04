package zkh.tool.excel.demo.reader;

import java.util.List;

import org.dom4j.DocumentException;

import net.sf.json.JSONArray;
import zkh.tool.excel.ExcelImport;
import zkh.tool.excel.demo.Apple;

public class ReaderTest {

	public static void main(String[] args) throws DocumentException {
		// 导入数据
		List<Apple> list;
		try {
			ExcelImport excelImport = new ExcelImport();
			list = excelImport.importData("F:\\test.xlsx", "F:\\Excels.xml", "Apple");
			System.out.println(JSONArray.fromObject(list));
			System.out.println(excelImport.errorList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
