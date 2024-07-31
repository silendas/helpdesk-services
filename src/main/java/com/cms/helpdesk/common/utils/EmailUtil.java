package com.cms.helpdesk.common.utils;

import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class EmailUtil {
    
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private ResourceLoader resourceLoader;


    public void sendEmail(String to, String subject, String text, boolean isHtml, String attachmentPath) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml);

            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                File attachment = new File(attachmentPath);
                if (attachment.exists()) {
                    helper.addAttachment(attachment.getName(), new FileSystemResource(attachment));
                }
            }

            emailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error", e);
        }
    }

    // public  String readHtmlTemplate(String templateFileName) {
    //     Resource resource = resourceLoader.getResource("classpath:templates/" + templateFileName);
    //     try (InputStream inputStream = resource.getInputStream()) {
    //         return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    //     } catch (IOException e) {
    //         log.error("error", e);
    //         throw new RuntimeException(e);
    //     }
    // }
}
