package com.example.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender sender;

    @Async
    public void send(String mailTo, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("onboarding@resend.dev");
        message.setSubject(subject);
        message.setTo(mailTo);
        message.setText(body);
        sender.send(message);
    }

}
