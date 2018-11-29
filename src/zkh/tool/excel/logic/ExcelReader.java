package zkh.tool.excel.logic;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import org.dom4j.Element;

import zkh.tool.date.DateUtil;
import zkh.tool.xml.XmlUtil;

/**
 * POI导入格式Excel文件
 *
 * 赵凯浩
 * 2018年11月16日 下午3:56:12
 */
public class ExcelReader {

	/**Xml配置参数**********************************************************************/
	// 最小长度（字符串）
	private String minLength = "0";
	// 最大长度（字符串）
	private String maxLength = "0";
	// 最小值（数字）
	private String minValue = "0";
	// 最达值（数字）
	private String maxValue = "0";
	// 是否必须，默认不必须
	private String required = "false";
	// （特殊）日期格式
	private String dateFormat = "";
	// 先转为指定格式的日期字符串
	private String dateStr = "";
	
	// 错误信息集合
	public List<String> errorList = new ArrayList<String>();
	// 行错误信息
	private String rowErrorMsgBase = null;
	// Excel当前行下标（从0开始）
	private int rowIndex = 0;
	// Excel当前行错误信息
	private String rowErrorMsg;
	// Excel当前行中列下标（从0开始）
	private int cellIndex = 0;
	// Excel当前行中列错误信息
	private String cellErrorMsg = "";
	// Excel当前行中列字母下标+"列"
	private String cellLetterIndex = "";
	
	
	/**
	 * 读取excel文件
	 * @param excelPath excel文件路径
	 * @param xmlPath xml文件路径
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @param workbook
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> readExcel(String excelPath, String xmlPath, String elementName, Workbook workbook) throws Exception{        
		/**1、Xml导入配置读取*************************************************************************/
		// 获取整体Document
		Document document = XmlUtil.getDocument(xmlPath);
		// 获取指定子节点
		Element element = XmlUtil.getElement(document, elementName);
        /**1、Xml导入配置读取*************************************************************************/
        
        /**2、Excel数据读取*************************************************************************/
 		// 最终组装的数据list
 		List<T> list = new ArrayList<T>(); Sheet sheet = null;
 		// 获得sheet（指定导入sheet下标数组）
 		String[] sheets = element.attributeValue("sheet") == null ? new String [] {} : element.attributeValue("sheet").split(",");
 		if(sheets.length > 0) {
 			for (int i = 0; i < sheets.length; i++) {
	 			// 得到一个工作表
		        sheet = workbook.getSheetAt(Integer.parseInt(sheets[i]) - 1);
		        // 获取行总数
		        int rows = sheet.getLastRowNum() + 1;
		        // 得到数据list
		        if(rows > 0) {list.addAll(getList(sheet, rows, element));};
			}
 		}else {
 			Iterator<Sheet> itSheet = workbook.sheetIterator();
 			while (itSheet.hasNext()) {
 				// 得到一个工作表
		        sheet = itSheet.next();
		        // 获取行总数
		        int rows = sheet.getLastRowNum() + 1;
		        // 得到数据list
		        if(rows > 0) {list.addAll(getList(sheet, rows, element));};
			}
 		}
        /**2、Excel数据读取*************************************************************************/
        return list;
	}
	
	/**
	 * 提取数据
	 * 描述：提取excel中的数据list
	 * @param sheet Excel中的指定sheet
	 * @param rows 当前sheet的总行数
	 * @param element 当前sheet的element
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> List<T> getList(Sheet sheet, int rows, Element element) throws Exception{
		List<T> list = new ArrayList<>(); T obj = null; Row row = null;
		
		// 获得Class
		Class<T> clazz = element.attributeValue("class") == null ? null : (Class<T>) Class.forName(element.attributeValue("class"));
        for (int i = 1; i < rows; i++) {
        	// 获取excel一行数据
            row = sheet.getRow(i);
            // 创建对象
            obj = clazz.newInstance();
            // 空行判断
            boolean isRowNull = iSRowNull(row);
            if(!isRowNull) {
            	// 组装bean对象
            	setObject(obj, clazz, row, element, false, false);            	
            	// 添加到集合中
            	list.add(obj);
            }
        }
        
        return list;
	}
	
	/**
	 * 创建对象
	 * @param obj 当前实例（可以是要导入的实体的对象，也可以是该实体里的对象或list中的实体对象）
	 * @param clazz 当前实例对象的class
	 * @param row sheet里的一行数据
	 * @param rows 当前sheet的总行数
	 * @param element 当前sheet的element
	 * @param listIndex 当前迭代的list的下标（如果是list里的对象）
	 * @return
	 * @throws Exception
	 */
	public <T> Object setObject(Object obj,Class<?> clazz,Row row, Element element, boolean isObject, boolean isList) throws Exception{
		// 每次开始新的一行时，重置行错误信息
		if(!isObject && !isList) {rowErrorMsg = "";};
		// Excel中当前行下标（从0开始）
		rowIndex = row.getRowNum();
		// 行错误信息
		rowErrorMsgBase = "[第" + (rowIndex+1) + "行]=>";
		
		/**1、简单属性处理*******************************************************************************/
		// 获取Element下指定名称的所有子节点
        List<Element> elements = XmlUtil.getElementList(element, "property");
        // 设置对象里的属性
        for (int i = 0; i < elements.size(); i++) {			
        	setValue(obj, clazz, row, elements.get(i), 0);
		}
        /**1、简单属性处理*******************************************************************************/
          
        /**2、Object属性处理***************************************************************************/
        List<Element> elementObjects = XmlUtil.getElementList(element, "object");
        Element elementObject = null;
        // 设置对象里的属性
        for (int i = 0; i < elementObjects.size(); i++) {
        	elementObject = elementObjects.get(i);
        	setObjectValue(obj, clazz, row, elementObject);
		}
        /**2、Object属性处理***************************************************************************/
        
        /**3、list属性处理****************************************************************************/
        List<Element> elementLists = XmlUtil.getElementList(element, "list");
        Element elementList = null;
        for (int i = 0; i < elementLists.size(); i++) {
        	elementList = elementLists.get(i);
        	setListValue(obj, clazz, row, elementList);
		}
        /**3、list属性处理****************************************************************************/
       
        /**4、记录行错误信息****************************************************************************/
        if(!isObject && !isList && StringUtils.isNotBlank(rowErrorMsg)) {        	
        	errorList.add(rowErrorMsgBase + rowErrorMsg);
        	rowErrorMsg = "";
        }
        /**4、记录行错误信息****************************************************************************/
        return obj;
	}
	
	/**
	 * 简单属性赋值
	 * @param obj 当前实例（可以是要导入的实体的对象，也可以是该实体里的对象或list中的实体对象）
	 * @param clazz 当前实例对象的class
	 * @param row sheet里的一行数据
	 * @param rows 当前sheet的总行数
	 * @param element 当前sheet的element
	 * @param num sheet中对应cell向后推移量
	 * @return
	 * @throws Exception
	 */
	public <T> T setValue(T obj,Class<?> clazz,Row row, Element element, int num) throws Exception{
		/**1、get、set方法操作*****************************************************************************/
		// 属性名称
		String fieldName = element.attributeValue("name");
		// 属性的get方法名称
		String getMethodName = getMethodName(fieldName, "get");
		// get方法
		Method getMethod = clazz.getMethod(getMethodName);
		// get方法返回类型
		Class<?> fieldMethodReturnClass = getMethod.getReturnType();
		// 属性的set方法名称
		String setMethodName = getMethodName(fieldName, "set");
		// set方法
		Method setMethod = clazz.getMethod(setMethodName, new Class[]{fieldMethodReturnClass});
		/**1、get、set方法操作*****************************************************************************/
		
		/**2、赋值**************************************************************************************/
		setMethod.invoke(obj, new Object[]{getCellValue(fieldMethodReturnClass, row, element, num)});
		/**2、赋值**************************************************************************************/
		return obj;
	}
	
	/**
	 * Object属性赋值
	 * @param obj 当前实例（可以是要导入的实体的对象，也可以是该实体里的对象或list中的实体对象）
	 * @param clazz 当前实例对象的class
	 * @param row sheet里的一行数据
	 * @param rows 当前sheet的总行数
	 * @param element 当前sheet的element
	 * @return
	 * @throws Exception
	 */
	public <T> T setObjectValue(T obj, Class<?> clazz, Row row, Element element) throws Exception{
		/**1、get、set方法操作*****************************************************************************/
		// 属性名称
		String fieldName = element.attributeValue("name");
		// 属性的get方法名称
		String getMethodName = getMethodName(fieldName, "get");
		// get方法
		Method getMethod = clazz.getMethod(getMethodName);
		// get方法返回类型
		Class<?> fieldMethodReturnClass = getMethod.getReturnType();
		// 属性的set方法名称
		String setMethodName = getMethodName(fieldName, "set");
		// set方法
		Method setMethod = clazz.getMethod(setMethodName, new Class[]{fieldMethodReturnClass});
		/**1、get、set方法操作*****************************************************************************/
		
		/**2、赋值**************************************************************************************/
		// 创建Object的对象
		Object temp = fieldMethodReturnClass.newInstance();
		temp = setObject(temp, fieldMethodReturnClass, row, element, false, true);
		setMethod.invoke(obj, new Object[]{temp});
		/**2、赋值**************************************************************************************/
		return obj;
	}
	
	/**
	 * List属性赋值
	 * @param obj 当前实例（可以是要导入的实体的对象，也可以是该实体里的对象或list中的实体对象）
	 * @param clazz 当前实例对象的class
	 * @param row sheet里的一行数据
	 * @param rows 当前sheet的总行数
	 * @param element 当前sheet的element
	 * @return
	 * @throws Exception
	 */
	public <T> Object setListValue(T obj,Class<?> clazz,Row row, Element element) throws Exception{
		/**1、get、set方法操作*****************************************************************************/
		// 属性名称
		String fieldName = element.attributeValue("name");
		// 属性的get方法名称
		String getMethodName = getMethodName(fieldName, "get");
		// get方法
		Method getMethod = clazz.getMethod(getMethodName);
		// get方法返回类型
		Class<?> fieldMethodReturnClass = getMethod.getReturnType();
		// 属性的set方法名称
		String setMethodName = getMethodName(fieldName, "set");
		// set方法
		Method setMethod = clazz.getMethod(setMethodName, new Class[]{fieldMethodReturnClass});
		/**1、get、set方法操作*****************************************************************************/
		
		/**2、赋值**************************************************************************************/
		// 2.1、创建List对象
		List<Object> tempList = new ArrayList<Object>();
		// 2.2、List的长度
		int size = Integer.parseInt(element.attributeValue("size"));
		// 2.3、得到泛型里的class类型对象
		ParameterizedType pt = (ParameterizedType) getMethod.getGenericReturnType();
		Class<?> listClass = (Class<?>)pt.getActualTypeArguments()[0];
		// 2.4、获取Element下指定名称的所有子节点
		List<Element> elementsList = XmlUtil.getElementList(element, "property");
		// 2.5、该list泛型对象在xml中配置的属性个数
		int elementsListSize = elementsList == null ? 0 : elementsList.size();
		// 2.6、如果list泛型的属性数量为0，setList(null)
		if(elementsListSize == 0) setMethod.invoke(obj, new Object[]{null});
		// 2.7、循环给list里添加指定个数的对象
		Object temp = null;
		for (int i = 0; i < size; i++) {
			// 创建一个list泛型里的对象
			temp = listClass.newInstance();
            // 设置list泛型对象的属性
            for (int j = 0; j < elementsList.size(); j++) {			
            	setValue(temp, listClass, row, elementsList.get(j), i*elementsListSize);
    		}
			tempList.add(temp);
		}
		setMethod.invoke(obj, new Object[]{tempList});
		/**2、赋值**************************************************************************************/
		return obj;
	}
	
	/**
	 * 得到Cell的值并转换成实体中属性的类型
	 * @param fieldClass 属性的class
	 * @param cell 当前列cell
	 * @param column 当前列的下标
	 * @return
	 * @throws Exception 
	 */
	private Object getCellValue(Class<?> fieldClass, Row row, Element element, int num) throws Exception{
		// Excel当前行的当前列下标（从0开始）
		cellIndex = strToNum(element.attributeValue("column")) + num;
		// 得到Excel行中指定列
		Cell cell = row.getCell(cellIndex);
		if(Date.class == fieldClass) {
			// 日期格式
			if(cell == null || cell.getDateCellValue() == null) {
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "date");
				return null;
			}else {		
				// 时间格式转换
				Date date = cell.getDateCellValue();
				// 指定的日期格式
				dateFormat = element.attributeValue("dateFormat");
				if(StringUtils.isBlank(dateFormat)) {				
					return date;
				}else {				
					// 先转为指定格式的日期字符串
					dateStr = DateUtil.dateToStr(date, dateFormat);
					// 再转为指定格式的日期
					SimpleDateFormat format = new SimpleDateFormat(dateFormat); 
					date = format.parse(dateStr);
				}
				return date;
			}
		}else if(boolean.class == fieldClass || Boolean.class == fieldClass){
			// 布尔类型
			if(cell == null || cell.getBooleanCellValue()) {
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "boolean");
				return false;
			}
			return cell.getBooleanCellValue();
		}else if(fieldClass == int.class || fieldClass == Integer.class){
			// 整数类型
			if(null == cell) {
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "int");
				return 0;
			}
			
			// 列错误信息添加到行
			rowErrorMsg += addCellErrorMsg(element, null, cell.getNumericCellValue(), "int");
			return (int)Math.floor(cell.getNumericCellValue());
		}else if(fieldClass == short.class || fieldClass == Short.class){
			// 短整形
			if(null == cell) {
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "int");
				return 0;
			}
			
			// 列错误信息添加到行
			rowErrorMsg += addCellErrorMsg(element, null, cell.getNumericCellValue(), "int");
			return (short)Math.floor(cell.getNumericCellValue());
		}else if(fieldClass == long.class || fieldClass == Long.class){
			//长整形类型
			if(null == cell) {
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "int");
				return 0l;
			}
			
			// 列错误信息添加到行
			rowErrorMsg += addCellErrorMsg(element, null, cell.getNumericCellValue(), "int");
			return (long)Math.floor(cell.getNumericCellValue());
		}else if(fieldClass == float.class || fieldClass == Float.class){
			// 单精度类型
			if(null == cell) {
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "int");
				return 0.0f;
			}
			
			// 列错误信息添加到行
			rowErrorMsg += addCellErrorMsg(element, null, cell.getNumericCellValue(), "int");
			return (float)Math.floor(cell.getNumericCellValue());
		}else if(fieldClass == double.class || fieldClass == Double.class){
			// 双精度类型
			if(null == cell) {
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "int");
				return 0.00d;
			}
			
			// 列错误信息添加到行
			rowErrorMsg +=  addCellErrorMsg(element, null, cell.getNumericCellValue(), "int");
			return Math.floor(cell.getNumericCellValue());
		}else if(fieldClass == BigDecimal.class) {
			// 大数字
			if(null == cell || null == cell.getStringCellValue()) {
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "int");
				return new BigDecimal(0);
			}
			
			// 列错误信息添加到行
			rowErrorMsg += addCellErrorMsg(element, null, null, "int");
			return new BigDecimal(cell.getStringCellValue());
		}else if(fieldClass == String.class){
			// Excel当前行中列为字符串的字符串
			String stringCellValue = "";
			// 字符串
			if(null == cell || null == cell.getStringCellValue()) {
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "string");
				return null;
			}else {
					stringCellValue = cell.getStringCellValue();
			}
			// 列错误信息添加到行
			rowErrorMsg += addCellErrorMsg(element, stringCellValue, null, "string");
			return stringCellValue;
		}else {
			return null;
		}
	}
	
	
	/**
	 * 得到列cell的下标
	 * 描述：A:65，B:66，C:67，D:68，E:69，F:70，G:71，H:72，I:73，J:74，K:75，L:76，M:77，N:78，O:79，P:80，Q:81，R:82，S:83，T:84，U:85，V:86，W:87，X:88，Y:89，Z:90
	 * @param column Xml文件中配置的字母形式下标
	 * @return
	 */
	public int strToNum(String column) {
		if(column == null) return -1;
		// 转大写
		column = column.toUpperCase();
		// 转字符数组
		char[] chars = column.toCharArray();
		// 计算得到最终的有效下标
		int index = 0;
		for (int i = 0; i < chars.length; i++) {
			index += ((int)chars[i]-65);
		}
		return index;
	}
	
	/**
	 * 得到列cell的下标
	 * 描述：A:65，B:66，C:67，D:68，E:69，F:70，G:71，H:72，I:73，J:74，K:75，L:76，M:77，N:78，O:79，P:80，Q:81，R:82，S:83，T:84，U:85，V:86，W:87，X:88，Y:89，Z:90
	 * @param column Xml文件中配置的字母形式下标
	 * @return
	 */
	public String numToStr(int num) {
		// 字母A65
		num += 65;
		// 数组长度
		int len = num/90 + (num%90 > 0 ? 1 : 0);
		// 转字符数组
		String chars = "";
		for (int i = 1; i <= len; i++) {
			if(i != len) {				
				chars += 'Z'; 
			}else {
				chars += (char)(num%90);
			}
		}
		return chars;
	}
	
	/**
	 * 获得属性的get/set方法名称
	 * @param fieldName 属性名称
	 * @param getOrSet "get"或"set"
	 * @return
	 */
	public String getMethodName(String fieldName, String getOrSet) {
		// 大写的属性首字母
		String initials = fieldName.substring(0, 1).toUpperCase();
		// 属性从第二位开始的名称
		String surplus = fieldName.length() > 1 ? fieldName.substring(1) : "";
		// 属性的get方法名称
		String getMethod = getOrSet + initials + surplus; 
		return getMethod;
	}
	
	/**
	 * row空行判断
	 * 描述：只判断前两个cell
	 * @param row 当前row行
	 * @param rows 总行数
	 * @return
	 */
	public boolean iSRowNull(Row row) {
		Cell cell = null;
		for (int i = 0; i < 2; i++) {
			cell = row.getCell(i);
			if(cell == null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 列表错误（验证）信息组装
	 * @param element
	 * @return
	 */
	public String addCellErrorMsg(Element element, String stringValue, Double numberValue, String type) {
		// 列编号
		cellLetterIndex = numToStr(cellIndex) + "列："; 
		// 重置列错误信息
		cellErrorMsg = "";
		// 是否可以为空
		required = element.attributeValue("required");
		if(StringUtils.isBlank(stringValue) && numberValue==null && "true".equals(required)) {
			cellErrorMsg += "不能为空，";
			
			if(StringUtils.isBlank(type)) {
			}else if("string".equals(type)) {
				// 长度最小值
				minLength = element.attributeValue("minLength");
				// 长度最大值
				maxLength = element.attributeValue("maxLength");
				
				if(StringUtils.isNotBlank(minLength) && StringUtils.isNotBlank(maxLength)) {
					cellErrorMsg += ("长度需大于" + minLength + "且小于" + maxLength + "，");
				}else if(StringUtils.isNotBlank(minLength)) {
					cellErrorMsg += ("长度不能小于" + minLength + "，");
				}else if(StringUtils.isNotBlank(maxLength)) {
					cellErrorMsg += ("长度不能大于" + maxLength + "，");
				}			
			}else if("int".equals(type)) {
				// 大小最小值
				minValue = element.attributeValue("minValue");
				// 大小最大值
				maxValue = element.attributeValue("maxValue");
				if(StringUtils.isNotBlank(minValue) && StringUtils.isNotBlank(maxValue)) {
					cellErrorMsg += ("数值需大于" + minValue + "且小于" + maxValue + "，");
				}else if(StringUtils.isNotBlank(minValue)) {
					cellErrorMsg += ("数值不能小于" + minValue + "，");
				}else if(StringUtils.isNotBlank(maxValue)) {
					cellErrorMsg += ("数值不能大于" + maxValue + "，");
				}
			}
		}
		
		if(StringUtils.isBlank(type)) {
		}else if("string".equals(type)) {
			// 长度最小值
			minLength = element.attributeValue("minLength");
			if(StringUtils.isNotBlank(minLength) && StringUtils.isNotBlank(stringValue) && stringValue.length() < Integer.parseInt(minLength)) {
				cellErrorMsg += ("长度不能小于" + minLength + "，");
			}
			// 长度最大值
			maxLength = element.attributeValue("maxLength");
			if(StringUtils.isNotBlank(maxLength) && StringUtils.isNotBlank(stringValue) && stringValue.length() > Integer.parseInt(maxLength)) {
				cellErrorMsg += ("长度不能大于" + maxLength + "，");
			}			
		}else if("int".equals(type)) {
			// 大小最小值
			minValue = element.attributeValue("minValue");
			if(StringUtils.isNotBlank(minValue) && null != numberValue && (int)Math.floor(numberValue) < Integer.parseInt(minValue)) {
				cellErrorMsg += ("数值不能小于" + minValue + "，");
			}
			// 大小最大值
			maxValue = element.attributeValue("maxValue");
			if(StringUtils.isNotBlank(maxValue) && null != numberValue && (int)Math.floor(numberValue) > Integer.parseInt(maxValue)) {
				cellErrorMsg += ("数值不能大于" + maxValue + "，");
			}
		}
		
		cellErrorMsg = cellErrorMsg.length() > 0 ? (cellLetterIndex + cellErrorMsg.substring(0, cellErrorMsg.length()-1) + "；") : "";
		return cellErrorMsg;
	}
	
} 
