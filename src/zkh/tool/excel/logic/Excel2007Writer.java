package zkh.tool.excel.logic;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import zkh.bean.DataResult;
import zkh.tool.date.common.Format;
import zkh.tool.list.ListSplitUtil;

/**
 * POI导出xlsx等格式Excel文件
 *
 * 赵凯浩
 * 2018年11月8日 下午2:09:20
 */
public class Excel2007Writer implements ExcelWriter{
	
	/**
	 * 使用poi通过反射导出Excel(通用方法)
	 * @param data
	 * @param headerNames
	 * @param fieldNames
	 * @param fileName
	 * @param format
	 * @param pageSize
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public <T> DataResult writer(List<T> data, String [] headerNames, String [] fieldNames, String fileName, String format, int pageSize, HttpServletResponse response) throws Exception{
    	DataResult result = new DataResult();

    	// 创建Excel工具
    	XSSFWorkbook wb = new XSSFWorkbook();;
    	try {
    		// 设置请求
    		response.setContentType("application/application/vnd.ms-excel");
			response.setHeader(
					"Content-disposition", 
					"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")
			);
			
	        pageSize = (pageSize <= 0 || pageSize >= 1000000) ? 1000000 : pageSize;
	        List<List<T>> list =  ListSplitUtil.split(data, pageSize);
	        for (int i = 0; i < list.size(); i++) {
				createSheet(wb, i, list.get(i), headerNames, fieldNames, fileName, format);
			}
	        
	        result.setSuccess(true);
		} catch (Exception e) {	
			result.setSuccess(false);
			e.printStackTrace();
		} finally {
			// 获取输出流
			OutputStream out  = response.getOutputStream();
			// 将Excel流写入输出流
            wb.write(out);
            // 关闭Excel流
            wb.close();
			// 刷新输出流
            out.flush();
            // 关闭输出流
            out.close();
		}
    	
        return result;
    }

    /**
     * 新建一个sheet数据表格
     * 描述：Excel的一个子页面
     * @param wb
     * @param sheetNum
     * @param data
     * @param headerNames
     * @param fieldNames
     * @param fileName
     * @param format
     * @throws Exception
     */
    private <T> void createSheet(XSSFWorkbook wb, int sheetNum, List<T> data, String [] headerNames, String [] fieldNames, String fileName, String format) throws Exception {
        // 创建一个sheet表格
    	XSSFSheet sheet = wb.createSheet("第" + ++sheetNum + "页");
    	// 获取第一行
    	XSSFRow row = sheet.createRow(0); 
    	// 默认列宽
        sheet.setDefaultColumnWidth(15);
        // Cell样式
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        // 粗体
        font.setBold(true);
        // 选择需要用到的字体格式
        cellStyle.setFont(font);
        // 设置标题（在第一行设置每个字段的标题）
    	for (int i = 0; i < headerNames.length; i++) {
    		XSSFCell cell = row.createCell((short) i);
    		cell.setCellValue(headerNames[i]);
		}
    	
    	// 写入数据
    	Iterator<T> it = data.iterator();
    	int index = 0;
    	while (it.hasNext()) {
            // 若不是在已有Excel表格后面追加数据 则使用该条语句
            row = sheet.createRow(++index);
            // 创建单元格，并设置值
            T t = (T) it.next();
            Class<? extends Object> tCls = t.getClass();
            for (short i = 0; i < fieldNames.length; i++) {
                XSSFCell cell = row.createCell((short) i);
                String fieldName = fieldNames[i];
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
                Object value = getMethod.invoke(t, new Object[] {});
                // 判断值的类型后进行强制类型转换
                typeRotation(cell, value, format);
            }
        }            
    }
    
    /**
     * 判断值的类型后进行强制类型转换
     * @param cell
     * @param value
     * @param format
     */
    private void typeRotation(XSSFCell cell, Object value, String format) {
    	String textValue = null;
        if (value instanceof Date) {
        	Date date = (Date) value;
        	format = StringUtils.isBlank(format)?Format.SIMPLE:format;
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            textValue = sdf.format(date);
        } else {
            // 其它数据类型都当作字符串简单处理
            if (value == null) {
                value = "";
            }
            textValue = value.toString();
        }
        if (textValue != null) {
            Pattern p = Pattern.compile("^//d+(//.//d+)?{1}quot;");
            Matcher matcher = p.matcher(textValue);
            if (matcher.matches()) {
                // 是数字当作double处理
                cell.setCellValue(Double.parseDouble(textValue));
            } else {
                cell.setCellValue(textValue);
            }
        }
    }

}
