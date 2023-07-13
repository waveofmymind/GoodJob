package com.goodjob.common.email.mailSender;

import jakarta.mail.MessagingException;

public interface EmailSenderService {

    void send(String to, String from, String title, String body) throws MessagingException;
}
