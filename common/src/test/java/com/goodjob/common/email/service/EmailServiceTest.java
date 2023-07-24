package com.goodjob.common.email.service;

import com.goodjob.common.email.entity.SendEmailLog;
import com.goodjob.common.email.mailSender.EmailSenderService;
import com.goodjob.common.email.repository.SendEmailLogRepository;
import com.goodjob.common.redis.RedisUt;
import com.goodjob.common.rsData.RsData;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;

import java.util.Optional;
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

    private static SendEmailLog getSendEmailLog(String resultCode) {
        SendEmailLog sendEmailLog = SendEmailLog.builder()
                .email("test@gmail.com")
                .subject("title")
                .body("body")
                .resultCode(resultCode)
                .build();
        return sendEmailLog;
    }


    private static String getVerificationCode() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String verificationCode = uuid.substring(0, 15);
        return verificationCode;
    }

    @Test
    @DisplayName("회원가입 시 인증코드 검증 성공")
    void verifySuccess() {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog("S-1");

        String verificationCode = getVerificationCode();

        doReturn(verificationCode).when(redisUt).getValue(sendEmailLog.getEmail());

        // WHEN
        RsData rsData = emailService.verifyCode(sendEmailLog.getEmail(), verificationCode);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("인증 코드가 확인되었습니다.");
        verify(emailService, times(1)).findByEmail(any(String.class));
        verify(redisUt, times(1)).getValue(any(String.class));
    }

    @Test
    @DisplayName("회원가입 시 인증코드 검증 실패 - 이메일 전송 오류")
    void verifyFail_EmailSendError() {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog("F-1");

        String verificationCode = getVerificationCode();

        doReturn(Optional.empty()).when(emailService).findByEmail(any(String.class));

        // WHEN
        RsData rsData = emailService.verifyCode(sendEmailLog.getEmail(), verificationCode);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("이메일 정보가 잘못되었습니다. 올바른 이메일을 입력하여 다시 시도해주세요.");
        verify(emailService, times(1)).findByEmail(any(String.class));
    }

    @Test
    @DisplayName("회원가입 시 인증코드 검증 실패 - 잘못된 검증 코드")
    void verifyFail_InvalidVerificationCode() {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog("S-1");

        String verificationCode = getVerificationCode();

        doReturn(Optional.of(sendEmailLog)).when(emailService).findByEmail(any(String.class));
        doReturn(verificationCode).when(redisUt).getValue(sendEmailLog.getEmail());

        // WHEN
        RsData rsData = emailService.verifyCode(sendEmailLog.getEmail(), "1234");

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("잘못된 인증 코드입니다.");
        verify(emailService, times(1)).findByEmail(any(String.class));
        verify(redisUt, times(1)).getValue(any(String.class));
    }

    @Test
    @DisplayName("회원가입 시 인증코드 검증 실패 - 레디스에 인증코드 없음")
    void verifyFail_MissingVerificationCodeInRedis() {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog("S-1");

        String verificationCode = getVerificationCode();

        doReturn(Optional.of(sendEmailLog)).when(emailService).findByEmail(any(String.class));
        doThrow(NullPointerException.class).when(redisUt).getValue(sendEmailLog.getEmail());

        // WHEN
        RsData rsData = emailService.verifyCode(sendEmailLog.getEmail(), verificationCode);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("이메일 정보가 잘못되었습니다. 올바른 이메일을 입력하여 다시 시도해주세요.");
        verify(emailService, times(1)).findByEmail(any(String.class));
        verify(redisUt, times(1)).getValue(any(String.class));
    }

    @Test
    @DisplayName("임시비밀번호발급 이메일 전송 성공")
    void sendPasswordEmailSuccess() throws MessagingException {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog(null);

        doNothing().when(emailSenderService)
                .send(any(String.class), any(String.class), any(String.class), any(String.class));

        // WHEN
        emailService.sendPasswordEmail("test", "test@gmail.com", "12345");

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
        SendEmailLog sendEmailLog = getSendEmailLog(null);
        String verificationCode = getVerificationCode();

        doNothing().when(emailSenderService)
                .send(any(String.class), any(String.class), any(String.class), any(String.class));

        // WHEN
        emailService.sendJoinEmail("test@gmail.com");

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
        SendEmailLog sendEmailLog = getSendEmailLog(null);

        doThrow(new MailSendException("mailException")).when(emailSenderService)
                .send(any(String.class), any(String.class), any(String.class), any(String.class));

        // WHEN
        emailService.sendPasswordEmail("test", "test@gmail.com", "12345");

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
        SendEmailLog sendEmailLog = getSendEmailLog(null);

        doThrow(new MessagingException("messingException")).when(emailSenderService)
                .send(any(String.class), any(String.class), any(String.class), any(String.class));

        // WHEN
        emailService.sendPasswordEmail("test", "test@gmail.com", "12345");

        // THEN
        assertThat(sendEmailLog.getResultCode()).isEqualTo("F-1");
        assertThat(sendEmailLog.getMessage()).isEqualTo("messingException");
        assertThat(sendEmailLog.getFailDate()).isNotNull();
        verify(emailSenderService, times(1))
                .send(any(String.class), any(String.class), any(String.class), any(String.class));
    }

}