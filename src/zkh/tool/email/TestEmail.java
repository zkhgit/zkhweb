package zkh.tool.email;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailAttachment;

public class TestEmail {

    public static void main(String[] args) {
        EmailInfo emailInfo = new EmailInfo();
        
        // 收件人
        List<String> toList = new ArrayList<String>();
        toList.add("1183277793@qq.com");
        emailInfo.setToAddress(toList);
        
        // 抄送人
        List<String> ccList = new ArrayList<String>();
        ccList.add("206909925@qq.com");
        emailInfo.setCcAddress(ccList);
        
        // 密送人
        List<String> bccList = new ArrayList<String>();
        bccList.add("908097699@qq.com");        
        emailInfo.setBccAddress(bccList);
        
        // 添加附件
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath("g:/dd.xls");
        attachment.setName("邮件测试.xlsx");
        List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
        attachments.add(attachment);
        emailInfo.setAttachments(attachments);
        
        // 标题和内容
        emailInfo.setSubject("邮件主题");
        emailInfo.setContent("内容：<h1>邮件,测试</h1><a href='http://edu.sstrc.gov.cn'>继续教育</a>");
         
        // 发送
        boolean flag = EmailUtil.send(emailInfo);
        System.out.println(flag ? "发送邮件成功" : "发送邮件失败");
    }
}