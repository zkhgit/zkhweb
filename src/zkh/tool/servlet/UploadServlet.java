package zkh.tool.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import zkh.tool.path.Date;
import zkh.tool.path.Request;
import zkh.tool.propertie.PropertieUtil;

/**
 * 文件上传
 * 描述：基于javax.servlet-3.0.jar、commons-io-2.4.jar、commons-fileupload-1.3.3.jar
 *
 * 赵凯浩
 * 2018年9月14日 下午4:29:45
 */
public class UploadServlet extends HttpServlet {
	
    private final static  long serialVersionUID = 1L;
    private final static Logger logger = LoggerFactory.getLogger(UploadServlet.class); 

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("进入上传文件---START---");
        
        // 1、获得配置文件路径
        String propertiesPath = Request.findProjectLowerPath(request, "resources\\config.properties"); 
        // 2、获取文件存放路径、是否开启日期路径（以及组装日期路径）
        String[] keys = new String[] {"zkh.fileupload.tempFilePath", "zkh.fileupload.datePath"};
        String[] propertiesValues = PropertieUtil.findValueByKeys(propertiesPath, keys);
        // 正常文件夹路径（+日期路径）
        String filePath = propertiesValues[0] + Date.path(null, propertiesValues[1]);
     
    	// 3.创建DiskFileItemFactory对象，配置缓存用
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        // 4. 创建 ServletFileUpload对象
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        // 5. 设置文件名称编码
        servletFileUpload.setHeaderEncoding("utf-8");
        // 6. 开始解析文件
        PrintWriter out = response.getWriter();
        String tempFilePath = null;
        List<String> tempFilePaths = new ArrayList<String>(); 
        try {
            List<FileItem> items = servletFileUpload.parseRequest(request);
            for (FileItem fileItem : items) {
                if (fileItem.isFormField()) { // >> 普通数据
                    String info = fileItem.getString("utf-8");
                    logger.info("普通数据: " + info);
                } else { // >> 文件
                    // 1. 获取文件名称、扩展名
                    String name = fileItem.getName();
                    String extension = name.substring(name.lastIndexOf(".")-1);
                    // 2. 获取文件的实际内容
                    InputStream is = fileItem.getInputStream();
                    // 3. 保存文件	
                    tempFilePath = filePath + "/" + UUID.randomUUID().toString().replace("-", "").toLowerCase() + extension;
                    FileUtils.copyInputStreamToFile(is, new File(tempFilePath));
                    // 将文件路径存入数组
                    tempFilePaths.add(tempFilePath);
                    logger.info("文件存放路径：" + tempFilePath);
                }

            }

            // 将文件路径数组以json字符串形式返回
            out.write(JSONArray.fromObject(tempFilePaths).toString());
        } catch (Exception e) {
        	e.printStackTrace();
        	out.print(false);
            logger.error("上传文件出错");
        } finally {
			out.flush();
			out.close();
			logger.info("退出上传文件---END---");
			logger.info("=====================================================");
		}

    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
}