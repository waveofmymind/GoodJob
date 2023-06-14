package com.goodjob.domain.email.service;

import com.goodjob.domain.email.entity.SendEmailLog;
import com.goodjob.domain.email.mailSender.EmailSenderService;
import com.goodjob.domain.email.repository.SendEmailLogRepository;
import com.goodjob.global.base.rsData.RsData;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
    private final SendEmailLogRepository emailLogRepository;
    private final EmailSenderService emailSenderService;

    @Transactional
    public RsData sendEmail(String email, String subject, String body, String verificationCode) {
        SendEmailLog sendEmailLog = SendEmailLog
                .builder()
                .email(email)
                .subject(subject)
                .body(body)
                .verificationCode(verificationCode)
                .build();

        emailLogRepository.save(sendEmailLog);

        RsData trySendRs = trySend(email, subject, body);

        setCompleted(sendEmailLog, trySendRs.getResultCode(), trySendRs.getMsg());

        return RsData.of("S-1", "메일이 발송되었습니다.", sendEmailLog.getId());
    }

    private RsData trySend(String email, String title, String body) {
        try {
            emailSenderService.send(email, "no-reply@no-reply.com", title, body);

            return RsData.of("S-1", "메일이 발송되었습니다.");
        } catch (MailException e) {
            return RsData.of("F-1", e.getMessage());
        } catch (MessagingException e) {
            return RsData.of("F-1", e.getMessage());
        }
    }

    public void setCompleted(SendEmailLog sendEmailLog, String resultCode, String message) {
        if (resultCode.startsWith("S-")) {
            sendEmailLog.setSendEndDate(LocalDateTime.now());
        } else {
            sendEmailLog.setFailDate(LocalDateTime.now());
        }

        sendEmailLog.setResultCode(resultCode);
        sendEmailLog.setMessage(message);

        emailLogRepository.save(sendEmailLog);
    }

    public Optional<SendEmailLog> findByVerificationCode(String verificationCode) {
        return emailLogRepository.findByVerificationCode(verificationCode);
    }
}
