package com.goodjob.core.domain.email.service;

import com.goodjob.core.domain.email.entity.SendEmailLog;
import com.goodjob.core.global.base.redis.RedisUt;
import com.goodjob.core.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailService emailService;
    private final RedisUt redisUt;

    @Async
    public void send(String email) {

        String title = "[이메일인증] GoodJob 이메일 인증 코드입니다. 코드를 입력하여 회원가입을 완료해주세요.";
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String verificationCode = uuid.substring(0, 10);

        // HTML 형식의 이메일 본문
        String body = "<html>"
                + "<head>"
                + "<title>이메일 인증 코드</title>"
                + "</head>"
                + "<body>"
                + "<h2>안녕하세요!</h2>"
                + "<p>회원가입을 완료하기 위한 이메일 인증 코드입니다.</p>"
                + "<p>아래의 인증 코드를 입력하여 회원가입을 완료해주세요.</p>"
                + "<p>인증 코드: <strong>" + verificationCode + "</strong></p>"
                + "<p>감사합니다.</p>"
                + "</body>"
                + "</html>";

        emailService.sendEmail(email, title, body, verificationCode);
    }

    public RsData verify(String email, String providedCode) {
        // 이메일이 오류 없이 보내졌는지 확인
        RsData emailResultCodeRsData = checkEmailResultCode(email);

        if (emailResultCodeRsData.isFail()) {
            return emailResultCodeRsData;
        }

        // 레디스에서 이메일을 키값으로 검증코드 반환
        try {
            String originalCode = redisUt.getValue(email);

            if (originalCode.equals(providedCode)) {
                return RsData.of("S-1", "인증 코드가 확인되었습니다.");
            }
            return RsData.of("F-1", "잘못된 인증 코드입니다.");
        } catch (NullPointerException e) {
            return RsData.of("F-1", "이메일 정보가 잘못되었습니다. 올바른 이메일을 입력하여 다시 시도해주세요.");
        }
    }

    private RsData checkEmailResultCode(String email) {
        Optional<SendEmailLog> opSendEmailLog = emailService.findByEmail(email);
        if (opSendEmailLog.isEmpty()) {
            return RsData.of("F-2", "이메일 정보가 잘못되었습니다. 올바른 이메일을 입력하여 다시 시도해주세요.");
        }

        SendEmailLog sendEmailLog = opSendEmailLog.get();
        String resultCode = sendEmailLog.getResultCode();

        if (resultCode.startsWith("F-")) {
            return RsData.of(resultCode, sendEmailLog.getMessage());
        }

        return RsData.of(resultCode, "메일이 성공적으로 발송되었습니다.");
    }
}
