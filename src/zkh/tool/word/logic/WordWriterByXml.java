package zkh.tool.word.logic;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;

/**
 * 导出Word文件
 *
 * 赵凯浩
 * 2019年1月24日 上午10:23:27
 */
public class WordWriterByXml {

	/**
	 * 导出Word
	 * @param fileName 导出的word文件的文件名（含后缀，后缀由xml模板决定）
	 * @param xmlPath xml模板文件的路径
	 * @param map 数据
	 * @param response
	 * @return
	 */
	public void export(String fileName, String xmlPath, Map<String, Object> map, HttpServletResponse response) throws Exception {
		int index = xmlPath.lastIndexOf("/");
		String root = xmlPath.substring(0, index);
		String xmlName = xmlPath.substring(index+1);
	    FileResourceLoader resourceLoader = new FileResourceLoader(root, "UTF-8");
	    Configuration configuration = Configuration.defaultConfiguration();
	    GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, configuration);
	    Template template = groupTemplate.getTemplate(xmlName);
	    template.binding("map", map); 
	    ByteArrayOutputStream tmp = new ByteArrayOutputStream();
	    tmp.write(template.render().getBytes());
	    // MIME
	    String mime = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	    if(fileName.toLowerCase().endsWith("doc")) { mime = "application/msword"; }
        response.setContentType(mime);
        response.setHeader("Content-disposition","attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
	    response.getOutputStream().write(tmp.toByteArray());
	}
	
}