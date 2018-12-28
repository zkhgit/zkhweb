package zkh.tool.email;

/**
 * 邮件Bean
 *
 * 赵凯浩
 * 2018年12月28日 上午10:26:02
 */
import java.util.List;

import org.apache.commons.mail.EmailAttachment;

/**
 * 邮件相关信息
 * 
 */
public class EmailInfo {
    
    // 收件人
    private List<String> toAddress = null;
    // 抄送人地址
    private List<String> ccAddress = null;
    // 密送人
    private List<String> bccAddress = null;
    // 邮件主题
    private String subject;
    // 邮件的文本内容
    private String content;
    // 附件信息
    private List<EmailAttachment> attachments = null;

    public List<String> getToAddress() {
		return toAddress;
	}
	public void setToAddress(List<String> toAddress) {
		this.toAddress = toAddress;
	}
	public List<String> getCcAddress() {
		return ccAddress;
	}
	public void setCcAddress(List<String> ccAddress) {
		this.ccAddress = ccAddress;
	}
	public List<String> getBccAddress() {
		return bccAddress;
	}
	public void setBccAddress(List<String> bccAddress) {
		this.bccAddress = bccAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<EmailAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<EmailAttachment> attachments) {
		this.attachments = attachments;
	}

}