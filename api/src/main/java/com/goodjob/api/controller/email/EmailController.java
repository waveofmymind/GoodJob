package com.goodjob.api.controller.email;

import com.goodjob.core.domain.email.service.EmailVerificationService;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailVerificationService emailVerificationService;
    private final Rq rq;

    @PostMapping("/send")
    public void sendEmail(String email) {
        // 쿠키에 해당 유저의 email 저장
        rq.setCookie("email", email);

        emailVerificationService.sendVerificationCode(email);
    }

    @PostMapping("/verify")
    public RsData verifyEmail(String verificationCode) {
        try {
            // 유저의 쿠키에서 이메일 값 가져옴
            Cookie emailCookie = rq.getCookie("email");

            return emailVerificationService.verify(emailCookie.getValue(), verificationCode);
        } catch (NullPointerException e) {
            return RsData.of("F-1", "이메일 정보가 없습니다. 새로고침 후 다시 시도해주세요.");
        }
    }
}
