package com.example.insurance.mis.mvp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender javaMailSender;

    public void mailAgentRegardingClaim(String toEmail, String subject, String body)
        throws Exception{
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("hi.ajay51@gmail.com");
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
       
        javaMailSender.send(mailMessage);

        log.info("Mail sent successfully to : "+ toEmail);
    }
}
