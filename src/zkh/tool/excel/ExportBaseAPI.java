package zkh.tool.excel;

import java.io.FileOutputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel导出常用API
 *
 * 赵凯浩
 * 2019年1月29日 上午9:20:42
 */
public class ExportBaseAPI {

	private Workbook workbook;
	@SuppressWarnings("rawtypes")
	private Drawing drawing; // 批注,暂时没用到
	
	public static void main(String[] args) {
		try {
			new ExportBaseAPI().export("20190129.xls", "复杂Excel测试");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean export(String fileName, String sheetName) throws Exception {
		// 初始化Workbook
		this.workbook = zkh.tool.excel.logic.Workbook.writer(fileName);
		// 设置默认工作表
		// workbook.setActiveSheet(2);
		// 重命名工作表
		// workbook.setSheetName(2, "新名字");
		// 初始化sheet
		initSheet(this.workbook, sheetName);
		// 通过输出流导出Excel
		FileOutputStream fileOut = new FileOutputStream("G:/excel/" + fileName);
		this.workbook.write(fileOut);
		fileOut.close();
        return true;
	}
	
	/**
	 * 初始化sheet
	 * @param workbook
	 * @param sheetName
	 */
	private void initSheet(Workbook workbook, String sheetName) {
		// 创建sheet
		Sheet sheet = workbook.createSheet(sheetName);		
		// 默认列宽
        sheet.setDefaultColumnWidth(15);
        
        // 设置第一列的宽度是31个字符宽度
        // sheet.setColumnWidth(1, 31 * 256);
        // 隐藏Excel网格线，默认值为true
        // sheet.setDisplayGridlines(false);
        // 打印时显示网格线，默认值为false
        // sheet.setGridsPrinted(true);
        // 组合行
        // sheet.groupRow(1, 3);
        // 取消行组合 
        // sheet.ungroupRow(1, 3);
        // 组合列
        // sheet.groupColumn(2, 7);
        // 取消列组合
        // sheet.ungroupColumn(1, 3);
        // 冻结行列
        // sheet.createFreezePane(2, 3, 15, 25);
        // 把第3行到第4行向下移动6行（正数表示向下移动，负数表示向上移动）
        // sheet.shiftRows(2, 4, 6);
        // 设置保护密码（禁止编辑）
        // sheet.protectSheet("password");

		// 得到打印对象
		// PrintSetup print = sheet.getPrintSetup();
		// true，则表示页面方向为横向；否则为纵向
		// print.setLandscape(false);
		// 缩放比例80%(设置为0-100之间的值)
		// print.setScale((short)80);
		// 设置页宽
		// print.setFitWidth((short)2);
		// 设置页高
		// print.setFitHeight((short)4);
		// 纸张设置
		// print.setPaperSize(PrintSetup.A4_PAPERSIZE);
		// 设置打印起始页码不使用"自动"
		// print.setUsePage(true);
		// 设置打印起始页码
		// print.setPageStart((short)6);
		// 设置打印网格线
		// sheet.setPrintGridlines(true);
		// 值为true时，表示单色打印
		// print.setNoColor(true);
		// 值为true时，表示用草稿品质打印
		// print.setDraft(true);
		// true表示“先行后列”；false表示“先列后行”
		// print.setLeftToRight(true);
		// 设置打印批注
		// print.setNotes(true);
		// Sheet页自适应页面大小
		// sheet.setAutobreaks(false);

        // 生成下拉式菜单
        String[] arrs = new String[] { "C++","Java", "C#" };
        CellRangeAddressList addressList = new CellRangeAddressList(0, 65535,0, 0);
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(arrs);
        DataValidation dataValidate = new HSSFDataValidation(addressList,dvConstraint);
        if(workbook instanceof XSSFWorkbook) {
        	DataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        	XSSFDataValidationConstraint dvConstraint1 = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint(arrs);
        	dataValidate = (XSSFDataValidation) dvHelper.createValidation(dvConstraint1, addressList);
        }
        sheet.addValidationData(dataValidate);
        
        // 在页面布局模式可见
		// 页眉
		Header header = sheet.getHeader();
		header.setLeft("页眉左边");
		header.setRight("页眉右边");
		header.setCenter("页眉中间");
		// 页脚
		Footer footer = sheet.getFooter();
		footer.setLeft("页脚左边");
		footer.setRight("页脚右边");
		footer.setCenter("页脚中间");
		
		// 批注
		this.drawing = sheet.createDrawingPatriarch();
		
		/**
         * 行列合并
         * 主要API：sheet.addMergedRegion(int firstRow, int lastRow, int firstCol, int lastCol)
         * 参数说明：起始行(firstRow)、结束行(lastRow)、起始列(firstCol)、结束列(lastCol)--行列下表都从0开始
         */
        // 合并第1行和第2行的第一列
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		// 合并第2行至第4行的第3列至第5列
		sheet.addMergedRegion(new CellRangeAddress(1, 3, 2, 4));
		
		// 添加数据
        addData(sheet);
	}
	
	/**
	 * 添加数据
	 * @param sheet
	 */
	private void addData(Sheet sheet){
		// 第1行数据
		addRow_1(sheet);
		// 第5行数据
		addRow_5(sheet);
		// 第6、7行
		addRow_6(sheet);
	}
	
	/**
	 * 添加第1行数据
	 * @param sheet
	 */
	private void addRow_1(Sheet sheet) {
		// 创建行
		Row row = sheet.createRow(0);
		// 行高
		row.setHeightInPoints(16.0F);
		// 设置行的高度是50个点
		// row.setHeightInPoints(50);
		
		// 单元格样式
		CellStyle style = null;
		// 格式化工具
		DataFormat dataFormat = this.workbook.createDataFormat();
		
		// 第一列(水平居中，垂直居下) + 批注
		Cell cell_0 = createCell(row, 0, (short)1, (short)1);
		cell_0.setCellValue("1|2行1列");
		cell_0.setCellComment(createComment("只是个批注", "张三", false));
		
		// 第2列(水平填充，垂直居中)
		Cell cell_1 = createCell(row, 1, (short)1, (short)1);
		cell_1.setCellValue("1列");
		
		// 第3列(水平居左，垂直居上)
		Cell cell_2 = createCell(row, 2, (short)1, (short)1);
		cell_2.setCellValue("1行3列");
		
		// 第4列(水平居右，垂直居顶)
		Cell cell_3 = createCell(row, 3, (short)1, (short)1);
		cell_3.setCellValue("1行4列");
		
		// 第5列 设置日期格式--使用Excel内嵌的格式
		Cell cell_4 = row.createCell(4);
		cell_4.setCellValue(new Date());
		// DateUtil.isCellDateFormatted(cell) // 判断单元格是否为日期
		style = this.workbook.createCellStyle();
		style.setDataFormat(dataFormat.getFormat("m/d/yy h:mm"));
		cell_4.setCellStyle(style);
		
		// 第6列 设置保留2位小数--使用Excel内嵌的格式
		Cell cell_5 = row.createCell(5);
		cell_5.setCellValue(12.3456789);
		style = this.workbook.createCellStyle();
		style.setDataFormat(dataFormat.getFormat("0.00"));
		cell_5.setCellStyle(style);
		
		// 第7列 设置货币格式--使用自定义的格式
		Cell cell_6 = row.createCell(6);
		cell_6.setCellValue(12345.6789);
		style = this.workbook.createCellStyle();
		style.setDataFormat(dataFormat.getFormat("￥#,##0"));
		cell_6.setCellStyle(style);
		
		// 第8列 设置百分比格式--使用自定义的格式
		Cell cell_7 = row.createCell(7);
		cell_7.setCellValue(0.123456789);
		style = this.workbook.createCellStyle();
		style.setDataFormat(dataFormat.getFormat("0.00%"));
		cell_7.setCellStyle(style);
		
		// 第9列 设置中文大写格式--使用自定义的格式
		Cell cell_8 = row.createCell(8);
		cell_8.setCellValue(12345);
		style = this.workbook.createCellStyle();
		style.setDataFormat(dataFormat.getFormat("[DbNum2][$-804]0"));
		cell_8.setCellStyle(style);
		
		// 第10列 设置科学计数法格式--使用自定义的格式
		Cell cell_9 = row.createCell(9);
		cell_9.setCellValue(12345);
		style = this.workbook.createCellStyle();
		style.setDataFormat(dataFormat.getFormat("0.00E+00"));
		cell_9.setCellStyle(style);
	}
	
	/**
	 * 添加第5行
	 * @param sheet
	 */
	private void addRow_5(Sheet sheet) {
		// 创建行
		Row row = sheet.createRow(4);
		// 行高
		row.setHeightInPoints(16.0F);
		
		// 第1列 基本计算
		Cell cell = row.createCell(0);
		cell.setCellFormula("2+3*4"); // 设置公式
		cell = row.createCell(1);
		cell.setCellValue(10);
		cell = row.createCell(2);
		cell.setCellFormula("A5*B5"); // 设置公式 (5为所在行，从1开始)
	}
	
	/**
	 * 添加第6行，计算值填入第7行
	 * @param sheet
	 */
	private void addRow_6(Sheet sheet) {
		// 创建行
		Row row = sheet.createRow(5);
		// 行高
		row.setHeightInPoints(16.0F);
		
		// SUM函数
		row.createCell(0).setCellValue(1);
		row.createCell(1).setCellValue(2);
		row.createCell(2).setCellValue(3);
		row.createCell(3).setCellValue(4);
		row.createCell(4).setCellValue(5);
		row = sheet.createRow(6);
		row.createCell(0).setCellFormula("sum(A6,C6)"); // 等价于"A6+C6"
		row.createCell(1).setCellFormula("sum(B6:D6)"); // 等价于"B6+C6+D6"
	}
	
	/**
	 * 创建单元格并设置样式
	 * @param row
	 * @param index
	 * @param halign
	 * @param valign
	 * @return
	 */
	private Cell createCell(Row row, int column, short align, short valign) {
		Cell cell = row.createCell(column);
		
		// 创建单元格样式
		CellStyle cellStyle = this.workbook.createCellStyle();
		
		// 设置字体
		Font font = this.workbook.createFont();
		// 设置字体名称
		font.setFontName("华文行楷");
		// 设置字号
		font.setFontHeightInPoints((short)14);
		// 设置字体颜色
		if(this.workbook instanceof HSSFWorkbook) {			
			font.setColor((short)1);
		}else {
			font.setColor((short)1);
		}
		
		/**
		 * 1.单下划线 FontFormatting.U_SINGLE
		 * 2.双下划线 FontFormatting.U_DOUBLE
		 * 3.会计用单下划线 FontFormatting.U_SINGLE_ACCOUNTING
		 * 4.会计用双下划线 FontFormatting.U_DOUBLE_ACCOUNTING
		 * 5.无下划线 FontFormatting.U_NONE
		 */
		// 设置下划线
		font.setUnderline(FontFormatting.U_SINGLE);
		
		/**
		 * 1.上标 FontFormatting.SS_SUPER
		 * 2.下标 FontFormatting.SS_SUB
		 * 3.普通，默认值 FontFormatting.SS_NONE
		 */
		// 设置上标下标
		font.setTypeOffset(FontFormatting.SS_SUPER);
		// 设置删除线
		font.setStrikeout(false);
		cellStyle.setFont(font);
		
		/**
		 * 1.如果是左侧对齐就是 HSSFCellStyle.ALIGN_FILL;
		 * 2.如果是居中对齐就是 HSSFCellStyle.ALIGN_CENTER;
		 * 3.如果是右侧对齐就是 HSSFCellStyle.ALIGN_RIGHT;
		 * 4.如果是跨列举中就是 HSSFCellStyle.ALIGN_CENTER_SELECTION;
		 * 5.如果是两端对齐就是 HSSFCellStyle.ALIGN_JUSTIFY;
		 * 6.如果是填充就是 HSSFCellStyle.ALIGN_FILL;
		 */
		// 设置单元格水平方向对其方式
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		
		/**
		 * 1.如果是靠上就是 HSSFCellStyle.VERTICAL_TOP;
		 * 2.如果是居中就是 HSSFCellStyle.VERTICAL_CENTER;
		 * 3.如果是靠下就是 HSSFCellStyle.VERTICAL_BOTTOM;
		 * 4.如果是两端对齐就是 HSSFCellStyle.VERTICAL_JUSTIFY;
		 */
		// 设置单元格垂直方向对其方式
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		// 上边边框
		cellStyle.setBorderTop(BorderStyle.THIN);
		// 上边边框颜色
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		// 底部边框
		cellStyle.setBorderBottom(BorderStyle.THIN);
		// 底部边框颜色
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		// 左边边框
		cellStyle.setBorderLeft(BorderStyle.THIN);
		// 左边边框颜色
		cellStyle.setLeftBorderColor(IndexedColors.RED.getIndex());
		// 右边边框
		cellStyle.setBorderRight(BorderStyle.THIN);
		// 右边边框颜色
		cellStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
		
		// 自动换行
		cellStyle.setWrapText(false);
		
		// 缩进
		cellStyle.setIndention((short)1);
		
		// 文本旋转，这里的取值是从-90到90，而不是0-180度。
		cellStyle.setRotation((short)0);
		
		// 背景色
		// 设置图案背景色
		// cellStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		// 设置图案样式
		// cellStyle.setFillPattern(CellStyle.BIG_SPOTS);
		
		// 前景色
		// 设置图案颜色
		// cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		// 设置图案样式
		// cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		// 设置单元格样式
		cell.setCellStyle(cellStyle);
		return cell;
	}
	
	/**
	 * 创建批注
	 * @param content
	 * @param author
	 * @param visible
	 * @return
	 */
	public Comment createComment(String content, String author, boolean visible) {
		// 创建批注位置
		ClientAnchor anchor = this.drawing.createAnchor(0, 0, 0, 0, 5, 1, 8,3);
		// 创建批注
		Comment comment = this.drawing.createCellComment(anchor);
		if(this.workbook instanceof XSSFWorkbook) {
			comment.setString(new XSSFRichTextString(content));			
		}else {
			comment.setString(new HSSFRichTextString(content));
		}
		// 设置批注作者
		comment.setAuthor(author);
		// 设置批注默认显示
		comment.setVisible(visible);
		return comment;
	}
}