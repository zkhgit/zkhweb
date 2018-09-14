package zkh.tool.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

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

import zkh.tool.propertie.PropertieUtil;

/**
 * 文件上传
 * 描述：基于javax.servlet-3.0.jar、commons-io-2.4.jar、commons-fileupload-1.3.3.jar
 *
 * 赵凯浩
 * 2018年9月14日 下午4:29:45
 */
public class UploaderServlet extends HttpServlet {
	
    private final static  long serialVersionUID = 1L;
    private final static Logger logger = LoggerFactory.getLogger(UploaderServlet.class); 

    private String serverPath = "g:/test/";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        logger.info("进入后台...");
        String tempFilePath = PropertieUtil.findValueByKey("config.properties", "fileupload.tempFilePath");
        System.out.println("tempFilePath " + tempFilePath);
        response.setCharacterEncoding("utf-8");       

        // 1.创建DiskFileItemFactory对象，配置缓存用
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

        // 2. 创建 ServletFileUpload对象
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

        // 3. 设置文件名称编码
        servletFileUpload.setHeaderEncoding("utf-8");

        // 4. 开始解析文件
        try {
            List<FileItem> items = servletFileUpload.parseRequest(request);
            for (FileItem fileItem : items) {

                if (fileItem.isFormField()) { // >> 普通数据
                    String info = fileItem.getString("utf-8");
                    logger.info("info: " + info);
                } else { // >> 文件
                    // 1. 获取文件名称
                    String name = fileItem.getName();
                    // 2. 获取文件的实际内容
                    InputStream is = fileItem.getInputStream();

                    // 3. 保存文件	
                    FileUtils.copyInputStreamToFile(is, new File(serverPath + "/" + name));
                }

            }

            /**
             * 返回json
             */
            PrintWriter out = response.getWriter();
            out.write("{\"name\":\"舞蹈家\"}");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传文件出错");
        }

    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
