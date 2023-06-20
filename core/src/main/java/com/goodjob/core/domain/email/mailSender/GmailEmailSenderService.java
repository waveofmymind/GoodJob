package com.goodjob.core.domain.email.mailSender;

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

    @Override
    public void send(String to, String from, String title, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(title);
        helper.setText(body, true);

        javaMailSender.send(message);
    }
}
