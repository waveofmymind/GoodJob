package com.goodjob.core.domain.email.service;

import com.goodjob.core.domain.email.entity.SendEmailLog;
import com.goodjob.core.domain.email.mailSender.EmailSenderService;
import com.goodjob.core.domain.email.repository.SendEmailLogRepository;
import com.goodjob.core.global.base.redis.RedisUt;
import com.goodjob.core.global.base.rsData.RsData;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EmailService {
    private final SendEmailLogRepository emailLogRepository;
    private final EmailSenderService emailSenderService;
    private final RedisUt redisUt;
    private static final long VERIFICATION_CODE_TTL = 1000L * 60 * 5;

    @Transactional
    public void sendPasswordEmail(String email, String subject, String body) {
        SendEmailLog sendEmailLog = SendEmailLog
                .builder()
                .email(email)
                .subject(subject)
                .body(body)
                .build();

        emailLogRepository.save(sendEmailLog);

        RsData trySendRs = trySend(email, subject, body);
        setCompleted(sendEmailLog, trySendRs.getResultCode(), trySendRs.getMsg());
    }

    @Transactional
    public void sendJoinEmail(String email, String subject, String body, String verificationCode) {
        SendEmailLog sendEmailLog = SendEmailLog
                .builder()
                .email(email)
                .subject(subject)
                .body(body)
                .build();

        // 레디스에 verifyCode 저장
        redisUt.setValue(email, verificationCode, VERIFICATION_CODE_TTL);
        emailLogRepository.save(sendEmailLog);

        RsData trySendRs = trySend(email, subject, body);
        setCompleted(sendEmailLog, trySendRs.getResultCode(), trySendRs.getMsg());
    }

    private RsData trySend(String email, String title, String body) {
        try {
            emailSenderService.send(email, "no-reply@no-reply.com", title, body);

            return RsData.of("S-1", "메일이 발송되었습니다.");
        } catch (MailException e) {
            log.error("MailException= {}", e.getMessage());
            return RsData.of("F-1", e.getMessage());
        } catch (MessagingException e) {
            log.error("MessagingException= {}", e.getMessage());
            return RsData.of("F-1", e.getMessage());
        }
    }

    private void setCompleted(SendEmailLog sendEmailLog, String resultCode, String message) {
        if (resultCode.startsWith("S-")) {
            sendEmailLog.setSendEndDate(LocalDateTime.now());
        } else {
            sendEmailLog.setFailDate(LocalDateTime.now());
        }

        sendEmailLog.setResultCode(resultCode);
        sendEmailLog.setMessage(message);

        emailLogRepository.save(sendEmailLog);
    }

    public Optional<SendEmailLog> findByEmail(String email) {
        return emailLogRepository.findByEmail(email);
    }
}
