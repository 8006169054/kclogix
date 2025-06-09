package com.kclogix.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Properties;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import kainos.framework.core.lang.KainosMailException;
import kainos.framework.core.model.KainosMailDto;
import kainos.framework.core.model.KainosMailDto.FileAttachment;
import kainos.framework.core.model.KainosMailDto.ReportAttachment;

@Component
public class MailUtil {

//	@Autowired
//	private JavaMailSender mailSender;
	
	private Properties serverInfo; // 서버 정보
	private Authenticator auth; // 인증 정보
	
	public MailUtil() {
		serverInfo = new Properties();
		serverInfo.put("mail.smtp.host", "smtp.sg.aliyun.com");
		serverInfo.put("mail.smtp.port", "465");
		serverInfo.put("mail.smtp.starttls.enable", "true");
		serverInfo.put("mail.smtp.auth", "true");
		serverInfo.put("mail.smtp.debug", "true");
		serverInfo.put("mail.smtp.user", "noreply@kclogix.com");
		serverInfo.put("mail.smtp.password", "kclogix0407!");
		serverInfo.put("mail.smtp.socketFactory.port", "465");
		serverInfo.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		serverInfo.put("mail.smtp.socketFactory.fallback", "false");
		serverInfo.put("mail.smtp.ssl.trust", "smtp.sg.aliyun.com");
//		serverInfo.put("mail.smtp.ssl.protocols", "TLSv1.2");
		// 사용자 인증 정보
		auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("noreply@kclogix.com", "kclogix0407!");
			}
		};
	}
	
	/**
	 * 
	 * @param mailDto
	 * @return
	 * @throws KainosMailException
	 */
	public byte[] sendMessage(KainosMailDto mailDto) throws KainosMailException {
		try {
			Session session = Session.getInstance(serverInfo, auth);
			MimeMessage msg = new MimeMessage(session);
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
	        helper.setFrom(mailDto.getFrom());
	        helper.setSubject(mailDto.getSubject());
	        helper.setTo(mailDto.getTo().toArray(new InternetAddress[0]));
	        
	        if(mailDto.getCc().size() > 0)
	        	helper.setCc(mailDto.getCc().toArray(new InternetAddress[0]));
	        for (FileAttachment attachment : mailDto.getAttachment()) {
	        	if(attachment.getFile() instanceof MultipartFile file) 
	        		helper.addAttachment(file.getOriginalFilename(), file);
	        	else if(attachment.getFile() instanceof File file) 
	        		helper.addAttachment(file.getName(), file);
	        }
	        
	        for (ReportAttachment attachment : mailDto.getReports())
	        	helper.addAttachment(MimeUtility.encodeText(attachment.getFileName()), new ByteArrayResource(attachment.getFile()));
	        
	        helper.setText(mailDto.getMailbody(), mailDto.getHtml());
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        msg.writeTo(baos);
	        return baos.toByteArray(); 
        } catch (Exception e) {
            throw new KainosMailException(e);
        }
    }
	
}
