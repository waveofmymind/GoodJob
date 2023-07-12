package com.goodjob.common.email.service;

import com.goodjob.common.email.mailSender.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GmailEmailSenderService implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    private static final String SENDER_ADDRESS = "no-reply@goodjob.com";

    @Override
    public void send(String to, String from, String title, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom(from);
        helper.setReplyTo(SENDER_ADDRESS);
        helper.setSubject(title);
        helper.setText(body, true);

        javaMailSender.send(message);
    }
}


