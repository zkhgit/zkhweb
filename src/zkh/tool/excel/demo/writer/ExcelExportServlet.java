package zkh.tool.excel.demo.writer;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zkh.tool.excel.ExcelExport;
import zkh.tool.excel.demo.Apple;

/**
 * Excel导出
 *
 * 赵凯浩
 * 2018年11月8日 上午8:31:44
 */
public class ExcelExportServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] headreNames = new String[] {"ID", "名1称", "单价", "生产1日期"};
	    String[] fieldNames = new String[] {"id", "name", "price", "productionDate"};
	    String fileName = "测试导2007.xlsx";
		try {
			List<Apple> list = Apple.组装List();
			ExcelExport<Apple> excelExport = new ExcelExport<Apple>(fileName, response);
			excelExport.exportBigData("用户数据", list, headreNames, fieldNames);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
}
