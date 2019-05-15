package zkh.tool.excel.logic;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import zkh.tool.bean.BeanUtil;
import zkh.tool.list.ListSplitUtil;

/**
 * POI导出格式Excel文件
 *
 * 赵凯浩
 * 2018年11月8日 下午2:28:12
 */
public class ExcelWriter<T> {
    
    private static Logger logger = LoggerFactory.getLogger(ExcelWriter.class);
    
    // 外部传入参数
    private String [] headerNames; // 变量字段名
    private String [] fieldNames; // 数据字段名
    private String title; // sheet第一行的大标题
    private String format="yyyy-MM-dd HH:mm:ss"; // 时间格式化
    // 内部临时参数
    private Map<String, CellStyle> styles;
    private int sheetNum; // 当前sheet的下表，从0开始
    private List<T> data; // 当前sheet的数据list
    private int rownum; // row行下标
    private Workbook workbook;
    private CellStyle dataStyle = null; // 主数据单元格的格式
 	public String exceptionMsg = null; // 全局异常
    
    public ExcelWriter(String title, String [] headerNames, String [] fieldNames, Workbook workbook, Map<String, CellStyle> customStyles) {
        // 这些参数放在构造方法里是只是为了使用方便
        this.headerNames = headerNames;
        this.fieldNames = fieldNames;
        this.title = title;
        // 获取workbook
        this.workbook = workbook;
        // 准备Excel样式
        this.styles = createStyles(workbook, customStyles);
        // 初始化设置主对象单元格的样式
        this.dataStyle = (CellStyle)this.styles.get("data");
    }
    
    /**
     * 导出Excel
     */
    public boolean writer(List<T> data, String fileName, int pageSize, HttpServletResponse response) throws Exception{
        try {
            // 设置请求
            response.setContentType("application/application/vnd.ms-excel");
            response.setHeader("Content-disposition","attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 每个sheet存放几条数据
            if(fileName.endsWith(".xls")) {
                pageSize = (pageSize <= 0 || pageSize >=60000) ? 60000 : pageSize;
            }else {             
                pageSize = (pageSize <= 0 || pageSize >= 1000000) ? 1000000 : pageSize;
            }
            // 按pageSize分割主数据list
            List<List<T>> list =  ListSplitUtil.split(data, pageSize);
            for (int i = 0; i < list.size(); i++) {
                this.sheetNum = i;
                this.data = list.get(i);
                // 创建sheet，并存放数据
                createSheet();
            }
        } catch (Exception e) { 
            logger.error("public boolean writer(List<T> data, String fileName, int pageSize, HttpServletResponse response)");
            logger.error("导出Excel时: 导出Excel出错!");
            exceptionMsg = "导出Excel时: 导出Excel出错!";
            e.printStackTrace();
            return false;
        } finally {
            // 获取输出流
            OutputStream out  = response.getOutputStream();
            // 将Excel流写入输出流
            workbook.write(out);
            // 关闭Excel流
            workbook.close();
            // 刷新输出流
            out.flush();
            // 关闭输出流
            out.close();
        }
        return true;
    }

    /**
     * 新建一个sheet数据表格
     * 描述：Excel的一个子页面
     * @throws Exception
     */
    private void createSheet() {
        try {
            // 重置sheet的行下标
            this.rownum = 0;
            // 创建sheet表格
            Sheet sheet = this.workbook.createSheet("第" + ++this.sheetNum + "页");
            // 设置默认列宽
            sheet.setDefaultColumnWidth(15);
            // 大标题行设置
            if(StringUtils.isNotBlank(this.title)) {
                Row titleRow = sheet.createRow(rownum++);
                titleRow.setHeightInPoints(30.0F);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellStyle((CellStyle)this.styles.get("title"));
                titleCell.setCellValue(title);
                // 设置sheet的行占多列
                sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(), headerNames.length-1));
            }
            // 小标题行设置
            Row headerRow = sheet.createRow(rownum++);
            headerRow.setHeightInPoints(16.0F);
            for (int i = 0; i < this.headerNames.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellStyle((CellStyle)this.styles.get("header"));
                cell.setCellValue(this.headerNames[i]);
            }
            // 写入数据
            writeData(sheet);
        } catch (Exception e) {
            logger.error("private void createSheet()");
            logger.error("导出Excel时: 创建sheet出错!");
            exceptionMsg = "导出Excel时: 创建sheet出错!";
            e.printStackTrace();
        }
    }
    
    /**
     * 写入数据
     * 描述：给sheet中的cell赋值
     * @param sheet
     */
    private void writeData(Sheet sheet) {
    	try {
    		Iterator<T> it = this.data.iterator(); 
    		Class<? extends Object> tCls = null; // 当前对象的class  
    		String[] fieldNamesChild = null; // 主要为了处理"user.office.officeName"复杂属性
    		String fieldName = null; // 当前属性名称，可以是一级属性user，也可以是二三级属性 office.officeName
    		Method getMethod = null; // 当前一级属性的get方法
    		String[] change = null; // 类型转换（例如：1:男;2:女）
    		int len = -1; // '|'字符的下标
    		Row row = null; Cell cell = null;
    		while (it.hasNext()) {
    			// 获取当前对象
    			T t = (T) it.next();
    			// 创建当前行
    			row = sheet.createRow(rownum++);
    			// 本次数据导出的主对象的class,只在第一次调用时赋值
    			if(tCls == null)tCls = t.getClass();
    			for (short i = 0; i < this.fieldNames.length; i++) {
    				// 创建单元格
    				cell = row.createCell(i);
    				// 设置单元格的样式
    				cell.setCellStyle(this.dataStyle);
    				len = this.fieldNames[i].indexOf("|"); // |字符的下标
    				
    				// 是否含需要转换类型的参数
    				if(len!=-1) {
    					// 类型转换（例如：1:男;2:女）
    					change = this.fieldNames[i].substring(len+1).split(";");
    					// 获取属性名数组（针对复杂对象，否则可以不必转为数组）
    					fieldNamesChild = this.fieldNames[i].substring(0, len).split("\\.");
    				}else {
    					change = null;
    					// 获取属性名数组（针对复杂对象，否则可以不必转为数组）
    					fieldNamesChild = this.fieldNames[i].split("\\.");
    				}
    				// 获取属性名（如id、name、org.id）
    				fieldName = fieldNamesChild[0];
    				// 获取一级属性的get方法
    				getMethod = BeanUtil.getGetMethodByFieldName(tCls, fieldName);
    				// 获取一级属性的值
    				Object value = getMethod.invoke(t, new Object[] {});
    				// 获取多级对象的属性值，一般用得到，当对象属性为复杂对象（user.office.officeName）时，循环获取多级属性的最终值(officeName)
    				for (int j = 1; j < fieldNamesChild.length; j++) {
    					fieldName = fieldNamesChild[j];
    					value = getValueByKey(value, fieldName);
    				}
    				// 判断值的类型后进行强制类型转换
    				typeRotation(cell, value, change);
    			}
    		}
		} catch (Exception e) {
			logger.error("private void createCellSetValue(Sheet sheet)");
            logger.error("导出Excel时: 写入数据出错!");
            exceptionMsg = "导出Excel时: 写入数据出错!";
            e.printStackTrace();
		}
    }
    
    /**
     * 判断值的类型后进行强制类型转换
     * @param cell
     * @param value
     * @param change 字符转换 1:男;2:女
     */
    private void typeRotation(Cell cell, Object value, String[] change) {
        try {           
            String textValue = null;
            if (value instanceof Date) {
                Date date = (Date) value;
                SimpleDateFormat sdf = new SimpleDateFormat(this.format);
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
                	// 字符串
                	if(change != null) {
                		cell.setCellValue(textValue);
                		String[] temp = null;
                		for (int i = 0; i < change.length; i++) {
                			temp = change[i].split(":");
							if(temp.length > 0 && temp[0].equals(textValue)) {								
								// 字符转换处理（1:男;2:女）
								cell.setCellValue(temp[1]);
							}
						}
                	}else {                		
                		cell.setCellValue(textValue);
                	}
                }
            }
        } catch (Exception e) {
            logger.error("private void typeRotation(Cell cell, Object value)");
            logger.error("导出Excel时: 判断值的类型后进行强制类型转换时出错!");
            exceptionMsg = "导出Excel时: 判断值的类型后进行强制类型转换时出错!";
            e.printStackTrace();
        }
    }
    
    /**
     * 通过对象和属性名获取属性值
     * @param obj
     * @param key
     * @return
     * @throws Exception 
     */
    private Object getValueByKey(Object obj, String key) throws Exception {
    	Class<?> clazz = obj.getClass();
    	// 重组正确的Class（过滤类名后带着的类_$$_jvstfaa_1字符串）
    	String clazzStr = clazz.toString();
    	int _len = clazzStr.indexOf("_");
    	if(_len>0) {
    		clazz = Class.forName(clazzStr.substring(0, _len).replace("class ", ""));    		    		
    	}
    	// 获得属性的get方法
    	Method method = BeanUtil.getGetMethodByFieldName(clazz, key);
        // 拿到get方法上的时间格式化注解,时间类型时有用
		DateTimeFormat dateTimeFormat = method.getAnnotation(DateTimeFormat.class);
		if(dateTimeFormat == null || StringUtils.isBlank(dateTimeFormat.pattern())) {
			this.format = "yyyy-MM-dd";
		}else {
			this.format = dateTimeFormat.pattern();
		}
        Object value = method.invoke(obj, new Object[] {});    
        return value;
    }
    
    /**
     * 创建Cell样式
     * @param workbook
     * @param customStyles 外部自定义样式
     * @return
     */
    private Map<String, CellStyle> createStyles(Workbook workbook, Map<String, CellStyle> customStyles){
      Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
      
      try {
          // 大标题样式 
          CellStyle style = workbook.createCellStyle();
          if(customStyles == null || customStyles.get("title") == null) {
              style.setAlignment(HorizontalAlignment.CENTER); // 水平居中间
              style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
              Font titleFont = workbook.createFont();
              titleFont.setFontName("Arial");
              titleFont.setFontHeightInPoints((short)14);
//              titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // poi-4.x下不可用，poi-3.14下可用
              style.setFont(titleFont);
              styles.put("title", style);         
          }
          
          // 小标题的样式
          if(customStyles == null || customStyles.get("header") == null) {
              style = workbook.createCellStyle();
//            style.setAlignment(CellStyle.ALIGN_CENTER); // 水平居中间 poi-3.14
              style.setAlignment(HorizontalAlignment.CENTER); // 水平居中间
//            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中 poi-3.14
              style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
              style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
//            style.setFillPattern((short)1); // poi-3.14
              style.setFillPattern(FillPatternType.NO_FILL); // poi_4.x没测试
              Font headerFont = workbook.createFont();
              headerFont.setFontName("Arial");
              headerFont.setFontHeightInPoints((short)10);
//            headerFont.setBoldweight((short)700); // poi-3.14可用
              headerFont.setColor(IndexedColors.WHITE.getIndex());
              style.setFont(headerFont);
              styles.put("header", style);        
          }
          
          // 主数据列的样式
          if(customStyles==null || customStyles.get("data") == null) {        
              style = workbook.createCellStyle();
//            style.setAlignment(CellStyle.ALIGN_CENTER); // 水平居中间 poi-3.14
              style.setAlignment(HorizontalAlignment.CENTER); // 水平居中间
//            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中 poi-3.14
              style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
//            style.setBorderRight((short)1); // poi-3.14
              style.setBorderRight(BorderStyle.THIN); // poi-4.x没测试
              style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
//              style.setBorderLeft((short)1); // poi-3.14
              style.setBorderLeft(BorderStyle.THIN); // poi-4.x没测试
              style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
//              style.setBorderTop((short)1); // poi-3.14
              style.setBorderTop(BorderStyle.THIN); // poi-4.x没测试
              style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
//              style.setBorderBottom((short)1); // poi-3.14
              style.setBorderBottom(BorderStyle.THIN); // poi-4.x没测试
              style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
              Font dataFont = workbook.createFont();
              dataFont.setFontName("Arial");
              dataFont.setFontHeightInPoints((short)10);
              style.setFont(dataFont);
              styles.put("data", style);
          }
      } catch (Exception e) {
          logger.error("private Map<String, CellStyle> createStyles(Workbook workbook, Map<String, CellStyle> customStyles)");
          logger.error("导出Excel时: 初始化CellStyle出错!");
          exceptionMsg = "导出Excel时: 初始化CellStyle出错!";
          e.printStackTrace();
      }
      
      return styles;
    }
 
}