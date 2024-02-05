package com.example.techblogplatformapi.services.impl;



import com.example.techblogplatformapi.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {



    @Autowired
    private JavaMailSender mailSender;



    public void sendHtmlMail(String to, String subject, String content, boolean isContentHTML) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        message.setFrom("tresorlundimu@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, isContentHTML); // true to indicate it's HTML content

        mailSender.send(message);
    }
}
