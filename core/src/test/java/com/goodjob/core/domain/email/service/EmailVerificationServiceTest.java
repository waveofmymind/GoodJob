package com.goodjob.core.domain.email.service;

import com.goodjob.core.domain.email.entity.SendEmailLog;
import com.goodjob.core.global.base.redis.RedisUt;
import com.goodjob.core.global.base.rsData.RsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceTest {

    @InjectMocks
    private EmailVerificationService emailVerificationService;

    @Mock
    private EmailService emailService;

    @Mock
    private RedisUt redisUt;

    private static SendEmailLog getSendEmailLog(String resultCode) {
        SendEmailLog sendEmailLog = SendEmailLog.builder()
                .resultCode(resultCode)
                .email("test@gmail.com")
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

        doReturn(Optional.of(sendEmailLog)).when(emailService).findByEmail(any(String.class));
        doReturn(verificationCode).when(redisUt).getValue(sendEmailLog.getEmail());

        // WHEN
        RsData rsData = emailVerificationService.verify(sendEmailLog.getEmail(), verificationCode);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("인증 코드가 확인되었습니다.");
        verify(emailService, times(1)).findByEmail(any(String.class));
        verify(redisUt, times(1)).getValue(any(Object.class));
    }

    @Test
    @DisplayName("회원가입 시 인증코드 검증 실패 - 이메일 전송 오류")
    void verifyFail_EmailSendError() {
        // GIVEN
        SendEmailLog sendEmailLog = getSendEmailLog("F-1");

        String verificationCode = getVerificationCode();

        doReturn(Optional.empty()).when(emailService).findByEmail(any(String.class));

        // WHEN
        RsData rsData = emailVerificationService.verify(sendEmailLog.getEmail(), verificationCode);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-2");
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
        RsData rsData = emailVerificationService.verify(sendEmailLog.getEmail(), "1234");

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("잘못된 인증 코드입니다.");
        verify(emailService, times(1)).findByEmail(any(String.class));
        verify(redisUt, times(1)).getValue(any(Object.class));
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
        RsData rsData = emailVerificationService.verify(sendEmailLog.getEmail(), verificationCode);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("이메일 정보가 잘못되었습니다. 올바른 이메일을 입력하여 다시 시도해주세요.");
        verify(emailService, times(1)).findByEmail(any(String.class));
        verify(redisUt, times(1)).getValue(any(Object.class));
    }
}

