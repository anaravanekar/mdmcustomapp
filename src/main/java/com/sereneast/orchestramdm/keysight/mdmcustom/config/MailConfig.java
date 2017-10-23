package com.sereneast.orchestramdm.keysight.mdmcustom.config;

import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class MailConfig {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String protocol;
    private String auth;
    private String starttls;
    private String debug;

    @Bean
    public JavaMailSender javaMailService() {
        initialize();
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private void initialize(){
//        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        ClassLoader classLoader = AppUtil.class.getClassLoader();
        File file = new File(classLoader.getResource("mail.properties").getFile());
        String rootPath = file.getPath();
        String mailConfigPath = rootPath;
        Properties mailProperties = new Properties();
        try {
            mailProperties.load(new FileInputStream(mailConfigPath));
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Error configuring email",e);
        }
        host=mailProperties.getProperty("email.host");
        port=Integer.valueOf(mailProperties.getProperty("email.port"));
        username=mailProperties.getProperty("email.username");
        password=mailProperties.getProperty("email.password");
        protocol=mailProperties.getProperty("email.transport.protocol");
        auth=mailProperties.getProperty("email.smtp.auth");
        starttls=mailProperties.getProperty("email.smtp.starttls.enable");
        debug=mailProperties.getProperty("email.debug");
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.auth", auth);
        properties.setProperty("mail.smtp.starttls.enable", starttls);
        properties.setProperty("mail.debug", debug);
        return properties;
    }
}