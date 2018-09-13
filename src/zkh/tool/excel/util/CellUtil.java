package zkh.tool.excel.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import zkh.tool.excel.bean.CodeType;

/**
 * CELL公用方法
 *
 * 赵凯浩
 * 2018年8月16日 上午10:55:38
 */
public class CellUtil {

	/**
	 * 通过cell获取cell的值
	 * @param cell
	 * @return
	 */
	public static Object getValueByCell(Cell cell) throws Exception{
		Object value = null;
		
		switch (cell.getCellTypeEnum().toString()) {
	        case CodeType.STRING:
	        	value = cell.getRichStringCellValue().getString();
	            break;
	        case CodeType.NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	            	value = cell.getDateCellValue();
	            } else {
	            	value = cell.getNumericCellValue();
	            }
	            break;
	        case CodeType.BOOLEAN:
	        	value = cell.getBooleanCellValue();
	            break;
	        case CodeType.FORMULA:
	        	value = cell.getCellFormula();
	            break;
	        case CodeType.BLANK:
	        	value = "";
	            break;
	        default:
	        	value = "";
	    }
		
		return value;
	}
	
}
