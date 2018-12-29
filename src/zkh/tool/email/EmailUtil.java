package zkh.tool.email;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DataSourceResolver;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceCompositeResolver;
import org.apache.commons.mail.resolver.DataSourceFileResolver;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zkh.tool.propertie.PropertieUtil;

/**
 * 发送邮件Util
 * 描述：基于commons-email-1.5.jar、javax.mail-1.6.2.jar
 *
 * 赵凯浩
 * 2018年12月28日 上午10:27:38
 */
public class EmailUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);
	private static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$"); // 判断字符串是否为整数 
	
	/**
	 * 发送 邮件
	 * 描述：1、关键参数从配置文件读取
	 * 描述：2、Html格式，支持附件
	 * 描述：3、从配置文件读取基本配置信息
	 * @param emailInfo
	 * @return
	 */
	public static boolean send(EmailInfo emailInfo) {
		InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream("config.properties");
		String[] emailKeys = new String[] {
				"email.serverHost", // 服务器地址（QQ邮箱、网易等各不相同）
				"email.senderAddress", // 发送者账号地址（邮箱账号）
				"email.senderPassword", // 发送者账号密码或授权码
				"email.sslSmtpPort", // 发送SSL邮件（启用SSL的SMTP服务器的端口号，默认为标准端口465）
				"email.charset", // 设置邮件消息的字符集
				"email.socketConnectionTimeout", // 客户端从服务器建立连接的超时时间（毫秒）
				"email.socketTimeout", // 客户端从服务器读取数据的超时时间（毫秒）
				"email.debug", // 是否开启debug模式
				};
		String[] emailValues = PropertieUtil.findValueByKeys(is, emailKeys);
		return send(emailValues, emailInfo);
	}
	
    /**
     * 发送 邮件
     * 描述：1、全参数版
     * 描述：2、Html格式，支持附件
     * @param serverHost 服务器地址（QQ邮箱、网易等各不相同）
     * @param senderAddress 发送者账号地址（邮箱账号）
     * @param senderPassword 发送者账号密码或授权码 
     * @param sslSmtpPort 发送SSL邮件（启用SSL的SMTP服务器的端口号，默认为标准端口465）
     * @param charset 设置邮件消息的字符集
     * @param socketConnectionTimeout 客户端从服务器建立连接的超时时间（毫秒）
     * @param socketTimeout 客户端从服务器读取数据的超时时间（毫秒）
     * @param debug 是否开启debug模式
     * @param emailInfo
     * @return
     */
    public static boolean send(String[] emailValues, EmailInfo emailInfo) {
        try {
            ImageHtmlEmail email = new ImageHtmlEmail();
            
            // 设置服务器地址
            email.setHostName(emailValues[0]);
            // 设置发送者账号
            email.setFrom(emailValues[1]);
            // 认证发送者账号和密码
            email.setAuthentication(emailValues[1], emailValues[2]);
            
            // 设置发送SSL邮件时的端口号
            if (StringUtils.isNotBlank(emailValues[3])) {
            	email.setSSLOnConnect(true);
            	email.setSslSmtpPort(emailValues[3]);
            }
            // 设置邮件消息的字符集
            if(StringUtils.isNotBlank(emailValues[4])) {
            	email.setCharset(emailValues[4]);
            }else {
            	email.setCharset("UTF-8");            	
            }
            // 设置客户端从服务器建立连接的超时时间（毫秒）
            if(StringUtils.isNotBlank(emailValues[5]) && pattern.matcher(emailValues[5]).matches()) {
            	email.setSocketConnectionTimeout(Integer.parseInt(emailValues[5]));
            }
            // 设置客户端从服务器读取数据的超时时间（毫秒）
            if(StringUtils.isNotBlank(emailValues[6]) && pattern.matcher(emailValues[6]).matches()) {            	
            	email.setSocketTimeout(Integer.parseInt(emailValues[6]));
            }
            // 设置开启deBug模式（输出debug信息）
            if(StringUtils.isNotBlank(emailValues[7]) && emailValues[7].equals("true")) {            	
            	email.setDebug(true);
            }
            // 设置解析html中的本地图片和网络图片,添加DataSourceFileResolver用于解析本地图片
            DataSourceResolver[] dataSourceResolvers = 
    				new DataSourceResolver[]{new DataSourceFileResolver(),
    	    		new DataSourceUrlResolver(new URL("http://")),
    				new DataSourceUrlResolver(new URL("https://"))};
            email.setDataSourceResolver(new DataSourceCompositeResolver(dataSourceResolvers));
            
            // 设置标题
            email.setSubject(emailInfo.getSubject());
            // 设置内容
            email.setHtmlMsg(emailInfo.getContent());
                        
            // 添加附件
            List<EmailAttachment> attachments = emailInfo.getAttachments();
            if (null != attachments && attachments.size() > 0) {
                for (int i = 0; i < attachments.size(); i++) {
                    email.attach(attachments.get(i));
                }
            }
            // 收件人
            List<String> toAddress = emailInfo.getToAddress();
            if (null != toAddress && toAddress.size() > 0) {
                for (int i = 0; i < toAddress.size(); i++) {
                     email.addTo(toAddress.get(i));
                }
            }
            // 抄送人
            List<String> ccAddress = emailInfo.getCcAddress();
            if (null != ccAddress && ccAddress.size() > 0) {
                for (int i = 0; i < ccAddress.size(); i++) {
                     email.addCc(ccAddress.get(i));
                }
            }            
            // 密送人
            List<String> bccAddress = emailInfo.getBccAddress();
            if (null != bccAddress && bccAddress.size() > 0) {
                for (int i = 0; i < bccAddress.size(); i++) {
                    email.addBcc(bccAddress.get(i));
                }
            }
 
            email.send();
            return true;
        } catch (Exception e) {
        	logger.error(e.getMessage());
            return false;
        }
    }
}