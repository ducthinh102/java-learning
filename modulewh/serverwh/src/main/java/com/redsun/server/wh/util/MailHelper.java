package com.redsun.server.wh.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailHelper {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public void send(String to, String subject, String text) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		javaMailSender.send(simpleMailMessage);
	}
	
	public void sendWithAttachment(String to, String subject, String text, List<String> attachments) throws IOException, MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		 
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(text);
        for (String attachment: attachments) {
        	File file = new File(attachment);
        	if(file.exists()) {
        		//Path path=Paths.get(attachment);
                //Files.delete(path);
    			file.delete();
    			file = new File(attachment);
    		}
            mimeMessageHelper.addAttachment(file.getName(), file);
        }

        javaMailSender.send(mimeMessageHelper.getMimeMessage());
	}
	
}
