package com.goodjob.core.domain.email.service;

import com.goodjob.core.domain.email.entity.SendEmailLog;
import com.goodjob.core.domain.email.mailSender.EmailSenderService;
import com.goodjob.core.domain.email.repository.SendEmailLogRepository;
import com.goodjob.core.global.base.redis.RedisUt;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private SendEmailLogRepository sendEmailLogRepository;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private RedisUt redisUt;

    private static SendEmailLog getSendEmailLog() {
        SendEmailLog sendEmailLog = SendEmailLog.builder()
                .email("test@gmail.com")
                .subject("title")
                .body("body")
                .build();
        return sendEmailLog;
    }

    private static String getVerificationCode() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String verificationCode = uuid.substring(0, 15);
        return verificationCode;
    }

    @Test
    @DisplayName("임시비밀번호발급 이메일 전송 성공")
    void sendPasswordEmailSuccess() throws MessagingException {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog();

        doNothing().when(emailSenderService)
                .send(any(String.class), any(String.class), any(String.class), any(String.class));

        // WHEN
        emailService.sendPasswordEmail(sendEmailLog);

        // THEN
        assertThat(sendEmailLog.getResultCode()).isEqualTo("S-1");
        assertThat(sendEmailLog.getMessage()).isEqualTo("메일이 발송되었습니다.");
        assertThat(sendEmailLog.getSendEndDate()).isNotNull();
        verify(sendEmailLogRepository, times(2)).save(any(SendEmailLog.class));
        verify(emailSenderService, times(1))
                .send(any(String.class), any(String.class), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("회원가입 인증코드 이메일 전송 성공")
    void sendJoinEmailSuccess() throws MessagingException {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog();
        String verificationCode = getVerificationCode();

        doNothing().when(emailSenderService)
                .send(any(String.class), any(String.class), any(String.class), any(String.class));
//        doNothing().when()

        // WHEN
        emailService.sendJoinEmail(sendEmailLog, verificationCode);

        // THEN
        assertThat(sendEmailLog.getResultCode()).isEqualTo("S-1");
        assertThat(sendEmailLog.getMessage()).isEqualTo("메일이 발송되었습니다.");
        assertThat(sendEmailLog.getSendEndDate()).isNotNull();
        verify(sendEmailLogRepository, times(2)).save(any(SendEmailLog.class));
        verify(emailSenderService, times(1))
                .send(any(String.class), any(String.class), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("이메일 전송 실패 - MailException")
    void sendPasswordEmailFail_MailException() throws MailException, MessagingException {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog();

        doThrow(new MailSendException("mailException")).when(emailSenderService)
                .send(any(String.class), any(String.class), any(String.class), any(String.class));

        // WHEN
        emailService.sendPasswordEmail(sendEmailLog);

        // THEN
        assertThat(sendEmailLog.getResultCode()).isEqualTo("F-1");
        assertThat(sendEmailLog.getMessage()).isEqualTo("mailException");
        assertThat(sendEmailLog.getFailDate()).isNotNull();
        verify(emailSenderService, times(1))
                .send(any(String.class), any(String.class), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("이메일 전송 실패 - MessagingException")
    void sendPasswordEmailFail_MessagingException() throws MessagingException {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog();

        doThrow(new MessagingException("messingException")).when(emailSenderService)
                .send(any(String.class), any(String.class), any(String.class), any(String.class));

        // WHEN
        emailService.sendPasswordEmail(sendEmailLog);

        // THEN
        assertThat(sendEmailLog.getResultCode()).isEqualTo("F-1");
        assertThat(sendEmailLog.getMessage()).isEqualTo("messingException");
        assertThat(sendEmailLog.getFailDate()).isNotNull();
        verify(emailSenderService, times(1))
                .send(any(String.class), any(String.class), any(String.class), any(String.class));
    }
}