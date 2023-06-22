package com.goodjob.api.controller.email;

import com.goodjob.core.domain.email.service.EmailVerificationService;
import com.goodjob.core.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send")
    @ResponseBody
    public RsData sendEmail(String email) {
        if (email.length() < 5) {
            return RsData.of("F-1", "올바른 이메일을 입력하세요.");
        }

        return emailVerificationService.send(email);
    }

    @PostMapping("/verify")
    @ResponseBody
    public RsData verifyEmail(String verificationCode) {
        return emailVerificationService.verify(verificationCode);
    }
}
