package zkh.tool.word.logic;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import zkh.tool.bean.UserInfo;
import zkh.tool.bean.BeanUtil;
import zkh.tool.date.DateUtil;
import zkh.tool.xml.XmlUtil;
import zkh.tool.websocket.ImportProgress;

/**
 * POI导入格式Word文件中表格数据
 *
 * 赵凯浩
 * 2019年1月23日 上午8:21:15
 */
public class WordReader2003 {

	private static Logger logger = LoggerFactory.getLogger(WordReader2003.class);

	/**Xml配置参数**********************************************************************/
	// 最小长度（字符串）
	private String minLength = "0";
	// 最大长度（字符串）
	private String maxLength = "0";
	// 最小值（数字）
	private String minValue = "0";
	// 最达值（数字）
	private String maxValue = "0";
	// 是否唯一
	private String unique = "false";
	// 同一列的的属性值集合的Map
	private Map<String, List<String>> uniqueMap = new HashMap<String, List<String>>(); 
	// 同一列的的属性值集合
	private List<String> uniqueList = null;
	// 是否必须，默认不必须
	private String required = "false";
	// （特殊）日期格式
	private String dateFormat = "";
	
	// 错误信息集合
	public List<String> errorList = new ArrayList<String>();
	// 行错误信息
	private String rowErrorMsgBase = null;
	// 当前表格
	private Table table = null;
	// 当前表格总记录行数
	private int rows = 0;
	// 当前表格当前行
	private TableRow row = null;
	// 当前行下标（从0开始）
	private int rowIndex = 0;
	// 当前行错误信息
	private String rowErrorMsg;
	// 当前行中列下标（从0开始）
	private int cellIndex = 0;
	// 列编号：cellIndex + "列"
	private String cellLetterIndex = "";
	// 当前行中列错误信息
	private String cellErrorMsg = "";
	
	// 全局异常
	public String exceptionMsg = null;
	
	/**
	 * 读取word文件
	 * @param xmlFile 模板文件
	 * @param elementName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> readTable(String elementName, FileInputStream fileInputStream, File xmlFile) throws Exception {
		// 最终组装的数据list
		List<T> list = new ArrayList<T>();
		/**1、Xml导入配置读取*************************************************************************/
		// 获取整体Document
		Document document = XmlUtil.getDocument(xmlFile);
		// 获取指定子节点
		Element element = XmlUtil.getElement(document, elementName);
        /**1、Xml导入配置读取*************************************************************************/
		
		/**2、Word数据读取**************************************************************************/
		POIFSFileSystem pOIFSFileSystem = new POIFSFileSystem(fileInputStream);   
	    @SuppressWarnings("resource")
		HWPFDocument hWPFDocument = new HWPFDocument(pOIFSFileSystem);
	    // 得到文档的读取范围
	    Range range = hWPFDocument.getRange();
	    TableIterator it = new TableIterator(range);
	    while (it.hasNext()) {
	    	this.table = (Table) it.next();	       
	    	// 获取行总数
	        this.rows = this.table.numRows();
	        // 得到数据list
	        if(this.rows > 0) {list.addAll(getList(element));};
	    }
		/**2、Word数据读取**************************************************************************/
		return list;
	}
	
	/**
	 * 提取数据
	 * 描述：提取word中的数据list
	 * @param sheet word中的指定sheet
	 * @param rows 当前sheet的总行数
	 * @param element 当前sheet的element
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private <T extends Object> List<T> getList(Element element) {
		List<T> list = new ArrayList<>(); T obj = null;
		try {
			// 获得Class
			Class<T> clazz = element.attributeValue("class") == null ? null : (Class<T>) Class.forName(element.attributeValue("class"));
	        for (int i = 1; i < rows; i++) {
	        	// 获取word一行数据
	            this.row = table.getRow(i);
	            // 创建对象
	            obj = clazz.newInstance();
	            // 空行判断
	            boolean isRowNull = iSRowNull(row);
	            if(!isRowNull) {
	            	// 组装bean对象
	            	// word中当前行下标（从0开始）
	    			this.rowIndex = i;
	            	setObject(obj, clazz, element, false, false);            	
	            	// 添加到集合中
	            	list.add(obj);
	            }
	        }
		} catch (Exception e) {
			logger.error("private <T extends Object> List<T> getList(Element element)");
	        logger.error("导入word时: 提取数据出错!");
	        exceptionMsg = "导入word时: 提取数据出错!";
	        e.printStackTrace();
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
	private <T> Object setObject(Object obj, Class<?> clazz, Element element, boolean isObject, boolean isList) {
		try {
			// 每次开始新的一行时，重置行错误信息
			if(!isObject && !isList) {rowErrorMsg = "";};
			// 行错误信息前半部分
			rowErrorMsgBase = "[第" + (rowIndex+1) + "行]=>";
			
			/**1、简单属性处理*******************************************************************************/
			// 获取Element下指定名称的所有子节点
	        List<Element> elements = XmlUtil.getElementList(element, "property");
	        // 设置简单属性
	        for (int i = 0; i < elements.size(); i++) {			
	        	setValue(obj, clazz, elements.get(i), 0);
			}
	        /**1、简单属性处理*******************************************************************************/
	          
	        /**2、Object属性处理***************************************************************************/
	        List<Element> elementObjects = XmlUtil.getElementList(element, "object");
	        // 设置对象属性
	        for (int i = 0; i < elementObjects.size(); i++) {
	        	setObjectValue(obj, clazz, elementObjects.get(i));
			}
	        /**2、Object属性处理***************************************************************************/
	        
	        /**3、list属性处理****************************************************************************/
	        List<Element> elementLists = XmlUtil.getElementList(element, "list");
	        // 设置list属性
	        for (int i = 0; i < elementLists.size(); i++) {
	        	setListValue(obj, clazz, elementLists.get(i));
			}
	        /**3、list属性处理****************************************************************************/
	       
	        /**4、记录行错误信息****************************************************************************/
	        if(!isObject && !isList && StringUtils.isNotBlank(rowErrorMsg)) {
	        	errorList.add(rowErrorMsgBase + rowErrorMsg);
	        	rowErrorMsg = "";
	        }
	        /**4、记录行错误信息****************************************************************************/
	        // 获得当前用户，进度条使用
	        UserInfo userInfo  = new UserInfo();
			userInfo.setUserId("123");
	        // 进度条
	        new ImportProgress().sendToUser("验证数据，正在处理" + rowIndex + "/" + (rows-1) + rowErrorMsg, userInfo.getUserId());
		} catch (Exception e) {
			logger.error("private <T> Object setObject(Object obj,Class<?> clazz,Row row, Element element, boolean isObject, boolean isList)");
	        logger.error("导入word时: 创建对象出错!");
	        exceptionMsg = "导入word时: 创建对象出错!";
	        e.printStackTrace();
		}
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
	private <T> T setValue(T obj, Class<?> clazz, Element element, int num) {
		try {
			/**1、get、set方法操作*****************************************************************************/
			// 属性名称
			String fieldName = element.attributeValue("name");
			// 获得属性的Get方法
			Method getMethod = BeanUtil.getGetMethodByFieldName(clazz, fieldName);			
			// get方法返回类型
			Class<?> fieldMethodReturnClass = getMethod.getReturnType();
			// 获得属性的Set方法
			Method setMethod = BeanUtil.getSetMethodByFieldName(clazz, fieldName);
			/**1、get、set方法操作*****************************************************************************/
			
			/**2、赋值**************************************************************************************/
			setMethod.invoke(obj, new Object[]{getCellValue(clazz, fieldMethodReturnClass, element, num)});
			/**2、赋值**************************************************************************************/
		} catch (Exception e) {
			logger.error("private <T> T setValue(T obj,Class<?> clazz, Element element, int num)");
	        logger.error("导入word时: 简单属性赋值出错!");
	        exceptionMsg = "导入word时: 简单属性赋值出错!";
	        e.printStackTrace();
		}
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
	private <T> T setObjectValue(T obj, Class<?> clazz, Element element) {
		try {
			/**1、get、set方法操作*****************************************************************************/
			// 属性名称
			String fieldName = element.attributeValue("name");
			// 获得属性的Get方法
			Method getMethod = BeanUtil.getGetMethodByFieldName(clazz, fieldName);			
			// get方法返回类型
			Class<?> fieldMethodReturnClass = getMethod.getReturnType();
			// 获得属性的Set方法
			Method setMethod = BeanUtil.getSetMethodByFieldName(clazz, fieldName);
			/**1、get、set方法操作*****************************************************************************/
			
			/**2、赋值**************************************************************************************/
			// 创建Object的对象
			Object temp = fieldMethodReturnClass.newInstance();
			temp = setObject(temp, fieldMethodReturnClass, element, false, true);
			setMethod.invoke(obj, new Object[]{temp});
			/**2、赋值**************************************************************************************/
		} catch (Exception e) {
			logger.error("private <T> T setObjectValue(T obj, Class<?> clazz, Element element)");
	        logger.error("导入word时: Object属性赋值出错!");
	        exceptionMsg = "导入word时: Object属性赋值出错!";
	        e.printStackTrace();
		}
		return obj;
	}
	
	private <T> Object setListValue(T obj,Class<?> clazz, Element element) {
		try {
			/**1、get、set方法操作*****************************************************************************/
			// 属性名称
			String fieldName = element.attributeValue("name");
			// 获得属性的Get方法
			Method getMethod = BeanUtil.getGetMethodByFieldName(clazz, fieldName);
			// 获得属性的Set方法
			Method setMethod = BeanUtil.getSetMethodByFieldName(clazz, fieldName);
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
	            	setValue(temp, listClass, elementsList.get(j), i*elementsListSize);
	    		}
				tempList.add(temp);
			}
			setMethod.invoke(obj, new Object[]{tempList});
			/**2、赋值**************************************************************************************/
		} catch (Exception e) {
			logger.error("private <T> Object setListValue(T obj,Class<?> clazz, Element element)");
	        logger.error("导入word时: List属性赋值出错!");
	        exceptionMsg = "导入word时: List属性赋值出错!";
	        e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 得到Cell的值并转换成实体中属性的类型
	 * @param clazz 当前类（也可能是对象属性的类）的Class
	 * @param fieldClass 属性的class
	 * @param cell 当前列cell
	 * @param column 当前列的下标
	 * @return
	 * @throws Exception 
	 */
	private Object getCellValue(Class<?> clazz, Class<?> fieldClass, Element element, int num) {
		try {
			// word当前行的当前列下标（从0开始）
			cellIndex = strToNum(element.attributeValue("column")) + num;
			// 得到word行中指定列
			TableCell cell = this.row.getCell(cellIndex);
			Object cellValue = null;
			// 取得单元格的内容
            for(int k = 0; k < cell.numParagraphs(); k++){   
                Paragraph para = cell.getParagraph(k);
                String s = para.text();
                // 去除后面的特殊符号
                if(null!=s&&!"".equals(s)){
              	    s = s.substring(0, s.length()-1);
                }
                cellValue = s;
            } 
            
			if(Date.class == fieldClass) {
				// 日期格式
				if(cellValue == null) {
					// 列错误信息添加到行
					rowErrorMsg += addCellErrorMsg(element, null, null, "date");
					return null;
				}else {		
					Date date = null;
					String fieldName = element.attributeValue("name");
					Method getMethod = BeanUtil.getGetMethodByFieldName(clazz, fieldName);
					// 拿到get方法上的时间格式化注解
					DateTimeFormat dateTimeFormat = getMethod.getAnnotation(DateTimeFormat.class);
					if(dateTimeFormat == null || StringUtils.isBlank(dateTimeFormat.pattern())) {
						dateFormat = "yyyy-MM-dd";
					}else {
						dateFormat = dateTimeFormat.pattern();
					}
					if(StringUtils.isBlank(dateFormat)) {				
						date = DateUtil.strToDate(cellValue.toString(), null);
					}else {				
						// 先转为指定格式的日期字符串
						date = DateUtil.strToDate(cellValue.toString(), dateFormat);
					}
					return date;
				}
			}else if(boolean.class == fieldClass || Boolean.class == fieldClass){
				// 布尔类型
				if(cellValue == null) {
					// 列错误信息添加到行
					rowErrorMsg += addCellErrorMsg(element, null, null, "boolean");
					return false;
				}else if(cellValue instanceof Boolean){
					return cellValue;
				}else {
					rowErrorMsg += cellIndex + "列：" + "应该是Boolean型";
					return false;
				}
			}else if(fieldClass == int.class || fieldClass == Integer.class){
				// 整数类型
				if(null == cellValue) {
					// 列错误信息添加到行
					rowErrorMsg += addCellErrorMsg(element, null, null, "int");
					return 0;
				}
				
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, (Double)cellValue, "int");
				return (int)Math.floor((Double)cellValue);
			}else if(fieldClass == short.class || fieldClass == Short.class){
				// 短整形
				if(null == cellValue) {
					// 列错误信息添加到行
					rowErrorMsg += addCellErrorMsg(element, null, null, "int");
					return 0;
				}
				
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, (Double)cellValue, "int");
				return (short)Math.floor((Double)cellValue);
			}else if(fieldClass == long.class || fieldClass == Long.class){
				//长整形类型
				if(null == cellValue) {
					// 列错误信息添加到行
					rowErrorMsg += addCellErrorMsg(element, null, null, "int");
					return 0l;
				}
				
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, (Double)cellValue, "int");
				return (long)Math.floor((Double)cellValue);
			}else if(fieldClass == float.class || fieldClass == Float.class){
				// 单精度类型
				if(null == cellValue) {
					// 列错误信息添加到行
					rowErrorMsg += addCellErrorMsg(element, null, null, "int");
					return 0.0f;
				}
				
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, (Double)cellValue, "int");
				return (float)Math.floor((Double)cellValue);
			}else if(fieldClass == double.class || fieldClass == Double.class){
				// 双精度类型
				if(null == cellValue) {
					// 列错误信息添加到行
					rowErrorMsg += addCellErrorMsg(element, null, null, "int");
					return 0.00d;
				}
				
				// 列错误信息添加到行
				rowErrorMsg +=  addCellErrorMsg(element, null, (Double)cellValue, "int");
				return Math.floor((Double)cellValue);
			}else if(fieldClass == BigDecimal.class) {
				// 大数字
				if(null == cellValue) {
					// 列错误信息添加到行
					rowErrorMsg += addCellErrorMsg(element, null, null, "int");
					return new BigDecimal(0);
				}
				
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, null, null, "int");
				return new BigDecimal(cellValue.toString());
			}else if(fieldClass == String.class){
				// 字符串
				if(null == cellValue) {
					// 列错误信息添加到行
					rowErrorMsg += addCellErrorMsg(element, null, null, "string");
					return null;
				}
				// 列错误信息添加到行
				rowErrorMsg += addCellErrorMsg(element, cellValue.toString(), null, "string");
				return cellValue;
			}else {
				return null;
			}
		} catch (Exception e) {
			logger.error("private Object getCellValue(Class<?> fieldClass, Element element, int num)");
	        logger.error("导入word时: 得到Cell的值并转换成实体中属性的类型出错!");
	        exceptionMsg = "导入word时: 得到Cell的值并转换成实体中属性的类型出错!";
	        e.printStackTrace();
	        return null;
		}
	}
	
	/**
	 * row空行判断
	 * 描述：只判断前两个cell
	 * @param row 当前row行
	 * @param rows 总行数
	 * @return
	 */
	private boolean iSRowNull(TableRow row) {
		try {			
			TableCell cell = null;
			for (int i = 0; i < 2; i++) {
				cell = row.getCell(i);
				if(cell == null) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("private boolean iSRowNull(Row row)");
	        logger.error("导入word时: row空行判断出错!");
	        exceptionMsg = "导入word时: row空行判断出错!";
	        e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 得到列cell的数字下标
	 * 描述：A:65，B:66，C:67，D:68，E:69，F:70，G:71，H:72，I:73，J:74，K:75，L:76，M:77，N:78，O:79，P:80，Q:81，R:82，S:83，T:84，U:85，V:86，W:87，X:88，Y:89，Z:90
	 * @param column Xml文件中配置的字母形式下标
	 * @return
	 */
	private int strToNum(String column) {
		// 计算得到最终的有效下标
		int index = 0;
		try {			
			if(column == null) return -1;
			// 转大写
			column = column.toUpperCase();
			// 转字符数组
			char[] chars = column.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				index += ((int)chars[i]-65);
			}
		} catch (Exception e) {
			logger.error("private int strToNum(String column)");
	        logger.error("导入word时: 得到列cell的数字下标出错!");
	        exceptionMsg = "导入word时: 得到列cell的数字下标出错!";
	        e.printStackTrace();
		}
		return index;
	}
	
	/**
	 * 列表错误（验证）信息组装
	 * @param element
	 * @param stringValue
	 * @param numberValue
	 * @param type
	 * @return
	 */
	private String addCellErrorMsg(Element element, String stringValue, Double numberValue, String type) {
		try {
			// 列编号
			cellLetterIndex = cellIndex + "列："; 
			// 重置列错误信息
			cellErrorMsg = "";
			// 唯一性判断，是否允许重复
			unique = element.attributeValue("unique");
			// 是否可以为空
			required = element.attributeValue("required");
			if(StringUtils.isBlank(stringValue) && numberValue==null && "true".equals(required)) {
				// 不允许为null时
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
				
				// 同一列的的属性值集合存放null占位
				uniqueList.add(null);
			}else {
				// 允许为null时
				if(StringUtils.isBlank(type)) {
				}else if("string".equals(type) && StringUtils.isNotBlank(stringValue)) {
					// 字符串,允许为null且不为null时
					boolean upiqueFlag = false;
					if("true".equals(unique)) {
						// 同一列的的属性值集合
						uniqueList = uniqueMap.get(cellLetterIndex)==null ? new ArrayList<String>():uniqueMap.get(cellLetterIndex); String temp = null;
						for (int i = 0; i < uniqueList.size(); i++) {
							temp = uniqueList.get(i);
							if(stringValue.equals(temp)) {
								cellErrorMsg += "与第" + (i+2) + "行的值重复（其他验证信息以第" + (i+2) + "行为准），";
								upiqueFlag = true;
								break;
							}
						}
						// 同一列的的属性值集合
						uniqueList.add(stringValue);
					}
					if(!upiqueFlag) {
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
					}
				}else if("int".equals(type) && numberValue != null) {
					// 数值,允许为null且不为null时
					boolean upiqueFlag = false;
					if("true".equals(unique)) {
						// 同一列的的属性值集合
						uniqueList = uniqueMap.get(cellLetterIndex)==null ? new ArrayList<String>():uniqueMap.get(cellLetterIndex); String temp = null;
						for (int i = 0; i < uniqueList.size(); i++) {
							temp = uniqueList.get(i);
							if((numberValue+"").equals(temp)) {
								cellErrorMsg += "与第" + (i+2) + "行的值重复（其他验证信息以第" + (i+2) + "行为准），";
								break;
							}
						}
						// 同一列的的属性值集合
						uniqueList.add(numberValue + "");
					}
					if(!upiqueFlag) {						
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
				}
			}
			
			if("true".equals(unique)) {
				uniqueMap.put(cellLetterIndex, uniqueList);
			}
			cellErrorMsg = cellErrorMsg.length() > 0 ? (cellLetterIndex + cellErrorMsg.substring(0, cellErrorMsg.length()-1) + "；") : "";
		} catch (Exception e) {
			logger.error("private String addCellErrorMsg(Element element, String stringValue, Double numberValue, String type)");
	        logger.error("导入word时: 列表错误（验证）信息组装出错!");
	        exceptionMsg = "导入word时: 列表错误（验证）信息组装出错!";
	        e.printStackTrace();
		}
		return cellErrorMsg;
	}

}