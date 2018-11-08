package zkh.tool.excel.logic;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import zkh.bean.DataResult;

/**
 * Excel导出接口
 *
 * 赵凯浩
 * 2018年11月8日 下午2:28:12
 */
public interface ExcelWriter {
	
	public <T> DataResult writer(List<T> data, String [] headerNames, String [] fieldNames, String fileName, String format, int pageSize, HttpServletResponse response) throws Exception;
	
}
