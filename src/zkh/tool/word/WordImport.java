package zkh.tool.word;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import zkh.tool.word.logic.WordReader2003;
import zkh.tool.word.logic.WordReader2007;
 
/**
 * 导入Word
 *
 * 赵凯浩
 * 2019年1月23日 上午8:04:50
 */
public class WordImport<T> {
	
	private Logger logger = LoggerFactory.getLogger(WordImport.class);

	// 导入的数据
	public List<T> dataList = null;
	// 数据错误信息集合
	public List<String> errorList = null;
	// 异常提示信息
	public String exceptionMsg = null;

	/**
	 * 快捷导入
	 * @param file Word文件
	 * @param xmlPath Xml文件路径
	 * @param mobalName Xml模板中指定的导入子模版标签名
	 * @return
	 * @throws Exception
	 */
	public WordImport<T> importDataSimple(MultipartFile file, String xmlPath, String mobalName) throws Exception {
		File wordFile = null;
		// 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        wordFile = File.createTempFile(UUID.randomUUID() + "", prefix);
        // MultipartFile to File
        file.transferTo(wordFile);
        // 获取Excel导入模板文件
		Resource resource = new ClassPathResource(xmlPath);
		// 获取要导入数据库的数据集
		importData(wordFile, resource.getFile(), mobalName);
		return this;
	}
	
	/**
	 * 导入
	 * @param wordPath word文件路径
	 * @param xmlFile Xml文件路径
	 * @param mobalName Xml模板中指定的导入子模版标签名
	 * @return
	 */
	public void importData(File file, File xmlFile, String mobalName) {
		try{
			FileInputStream in = new FileInputStream(file);
			if(file.getName().toLowerCase().endsWith("docx")){
				WordReader2007 wordReader = new WordReader2007();
				this.dataList = wordReader.readTable(mobalName, in, xmlFile);
				this.errorList = wordReader.errorList;
				this.exceptionMsg = wordReader.exceptionMsg;
			} else {
				WordReader2003 wordReader = new WordReader2003();
				this.dataList = wordReader.readTable(mobalName, in, xmlFile);
				this.errorList = wordReader.errorList;
				this.exceptionMsg = wordReader.exceptionMsg;
			}
		}catch(Exception e){
			logger.error("public List<T> importTable(String wordPath, File xmlFile, String elementName)");
			logger.error("导入Word时: 导入出错!");
			exceptionMsg = "导入Word时: 导入出错!";
			e.printStackTrace();
		}
	}
	
}