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
    private final SendEmailLogRepository sendEmailLogRepository;
    private final EmailSenderService emailSenderService;
    private final RedisUt redisUt;
    private static final long VERIFICATION_CODE_TTL = 1000L * 60 * 5;

    @Transactional
    public void sendPasswordEmail(SendEmailLog sendEmailLog) {
        sendEmailLogRepository.save(sendEmailLog);

        RsData trySendRs = trySend(sendEmailLog);
        setCompleted(sendEmailLog, trySendRs.getResultCode(), trySendRs.getMsg());
    }

    @Transactional
    public void sendJoinEmail(SendEmailLog sendEmailLog, String verificationCode) {
        // 레디스에 verifyCode 저장
        redisUt.setValue(sendEmailLog.getEmail(), verificationCode, VERIFICATION_CODE_TTL);
        sendEmailLogRepository.save(sendEmailLog);

        RsData trySendRs = trySend(sendEmailLog);
        setCompleted(sendEmailLog, trySendRs.getResultCode(), trySendRs.getMsg());
    }

    public Optional<SendEmailLog> findByEmail(String email) {
        return sendEmailLogRepository.findByEmail(email);
    }
    public Optional<SendEmailLog> findByUsername(String username) {
        return sendEmailLogRepository.findByUsername(username);
    }

    private RsData trySend(SendEmailLog sendEmailLog) {
        try {
            emailSenderService.send(sendEmailLog.getEmail(), "no-reply@no-reply.com",
                    sendEmailLog.getSubject(), sendEmailLog.getBody());

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

        sendEmailLogRepository.save(sendEmailLog);
    }
}
