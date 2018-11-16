package zkh.tool.xml;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Xml工具
 *
 * 赵凯浩
 * 2018年11月15日 上午8:35:53
 */
public class XmlUtil {

	/**
	 * 通过xml文件路径得到整个Document
	 * @param filePath
	 * @return
	 */
	public static Document getDocument(String filePath) {
		// 1.创建一个xml解析器对象
		SAXReader reader = new SAXReader();
		// 2.读取xml文档，返回Document对象
		Document document = null;
		try {
			document = reader.read(new File(filePath));
		} catch (DocumentException e) {
			System.out.println("读取XML文件 " + filePath + " 时出错");
			e.printStackTrace();
		}
		return document;
	}
	
	/**
	 * 获取Document下指定名称的子节点
	 * 描述：通过节点名称
	 * @param elementName
	 * @param name
	 * @return
	 */
	public static Element getElement(Document document, String elementName) {
		Element element =  document.getRootElement();
		return element.element(elementName);
	}
	
	/**
	 * 获取Element下指定名称的所有子节点
	 * @param element
	 * @param elementName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getElementList(Element element, String elementName) {
		return element.elements(elementName);
	}
	
	/**
	 * 获取element下的指定属性值
	 * @param element
	 * @param attributeName
	 */
	public static void getAttributeValue(Element element, String attributeName) {
		element.attributeValue(attributeName);
	}
}
