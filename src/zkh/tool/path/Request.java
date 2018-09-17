package zkh.tool.path;

import javax.servlet.http.HttpServletRequest;

/**
 * servlet里request各种路径
 *
 * 赵凯浩
 * 2018年9月17日 上午9:21:34
 */
public class Request {
	
	/**
	 * 获得项目项目所在目录下一级（或多级）指定文件夹的绝对路径
	 * @param request
	 * @param lowerPathName
	 * @return
	 */
	public static String findProjectLowerPath(HttpServletRequest request, String lowerPathName) {
		// 项目路径（通常默认包含项目下一级路径\WebContent）
		String realPath = request.getSession().getServletContext().getRealPath("/");
		// 项目名
        String contextPath = request.getContextPath().substring(1);
        // 项目所在绝对路径
        realPath = realPath.substring(0, realPath.indexOf(contextPath.substring(1))) + contextPath.substring(1);
        // 项目下指定文件夹的路径
        String projectLowerPath = realPath + "\\" + lowerPathName; 
        return projectLowerPath;
	}

}
