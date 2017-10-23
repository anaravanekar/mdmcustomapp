package com.sereneast.orchestramdm.keysight.mdmcustom.email;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

@Service
public class EmailSender {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    
    @Resource(name="javaMailService")
    private JavaMailSender javaMailSender;

	private String fromAddress;
	
    public EmailStatus sendPlainText(String to, String subject, String text) {
        return sendM(to, subject, text, false);
    }
 
    public EmailStatus sendHtml(String to, String subject, String htmlBody) {
        return sendM(to, subject, htmlBody, true);
    }
 
    private EmailStatus sendM(String to, String subject, String text, Boolean isHtml) {
        try {
            ClassLoader classLoader = AppUtil.class.getClassLoader();
            File file = new File(classLoader.getResource("mail.properties").getFile());
            String rootPath = file.getPath();
            String mailConfigPath = rootPath;
            Properties mailProperties = new Properties();
            mailProperties.load(new FileInputStream(mailConfigPath));
            fromAddress = mailProperties.getProperty("email.from");
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to.split(","));
            helper.setSubject(subject);
            helper.setText(text, isHtml);
            if(StringUtils.isNotBlank(fromAddress)){
            	helper.setFrom(fromAddress);
            }
            javaMailSender.send(mail);
            LOGGER.info("Send email '{}' to: {}", subject, to);
            return new EmailStatus(to, subject, text).success();
        } catch (Exception e) {
            LOGGER.error(String.format("Problem with sending email to: {}, error message: {}", to, e.getMessage()));
            return new EmailStatus(to, subject, text).error(e.getMessage());
        }
    }
}
