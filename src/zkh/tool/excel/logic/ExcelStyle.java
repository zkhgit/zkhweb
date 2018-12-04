package zkh.tool.excel.logic;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * 自定义Excel样式
 * 描述：工具
 *
 * 赵凯浩
 * 2018年12月3日 上午8:26:20
 */
public class ExcelStyle {
	
	private CellStyle title; // 第一行的大标题样式
	private CellStyle header; // 第二行的小标题
	private CellStyle data; // 主数据
	
	public ExcelStyle() {
		super();
	}
	public ExcelStyle(CellStyle title, CellStyle header, CellStyle data) {
		super();
		this.title = title;
		this.header = header;
		this.data = data;
	}
	public CellStyle getTitle() {
		return title;
	}
	public void setTitle(CellStyle title) {
		this.title = title;
	}
	public CellStyle getHeader() {
		return header;
	}
	public void setHeader(CellStyle header) {
		this.header = header;
	}
	public CellStyle getData() {
		return data;
	}
	public void setData(CellStyle data) {
		this.data = data;
	}

}
