package com.goodjob.core.domain.email.service;

import com.goodjob.core.domain.email.entity.SendEmailLog;
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

    public RsData verify(String verificationCode) {
        Optional<SendEmailLog> opVerificationCode = emailService.findByVerificationCode(verificationCode);

        if (opVerificationCode.isEmpty()) {
            return RsData.of("F-1", "잘못된 인증 코드입니다.");
        }

        return RsData.of("S-1", "인증 코드가 확인되었습니다.");
    }
}
