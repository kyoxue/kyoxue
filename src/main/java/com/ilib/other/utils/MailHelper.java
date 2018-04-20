package com.ilib.other.utils;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ilib.utils.velocityUtil;
/**
 * 邮件发送
 * @author Kyoxue
 */
@Service
public class MailHelper {

	private final Logger log = LoggerFactory.getLogger(MailHelper.class);
    @Autowired
    private JavaMailSenderImpl mailSender;
	/**
	 * @param v vm模板的动态数据
	 * @param vm vm模板文件名
	 * @param toMail 接收方邮箱
	 * @param title 邮件标题
	 */
    public void sendTaskMail(VelocityContext v,String vm,String toMail,String title) {
    	try {
	    	MimeMessage msg = mailSender.createMimeMessage();
	    	//在建构 MimeMessageHelper 类别的实例时所给定的 boolean 值为 true，表示要启用 multipart 模式
	        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
	        helper.setFrom(mailSender.getUsername());
	        helper.setTo(toMail);
	        helper.setSubject(title);
	    	String templateFile ="vms/".concat(vm);
	    	String encoding ="utf-8";
	    	String content ="";
			try {
				content = velocityUtil.getTemplateVal(v, templateFile, encoding);
			} catch (Exception e1) {
				log.error("get mail content error!",e1);
				return;
			}
			/**
			 * 第二个参数true，表示text的内容为html
			 * 然后注意<img/>标签，src='cid:file'
			 * 'cid'是contentId的缩写
			 * 'file'是一个标记，需要在后面的代码中调用MimeMessageHelper的addInline方法替代成文件
			 */
			helper.setText(content,true);
	        //文件地址相对应src目录 邮件logo
//			FileSystemResource img =new FileSystemResource(new File("C:/upload/caterpillar.jpg"));
			//addInline正文邮件内容镶嵌图片，注意这种不一定每个邮箱都会显示图片，126可以，QQ邮箱这种垃圾就不行。
//	        ClassPathResource file = new ClassPathResource("kick.png",MailHelper.class.getClassLoader());
//	        helper.addInline("file", file);
	        //邮件附件，测试邮箱都可以126 qq
	        ClassPathResource attach = new ClassPathResource("attachs/kick.png");  
	        helper.addAttachment("kick.png", attach); 
	        mailSender.send(msg);
	        log.info("mail has been sent to " + toMail);
        } catch (MessagingException e) {
        	log.error("send mial failed!",e);
        }
    }

}
