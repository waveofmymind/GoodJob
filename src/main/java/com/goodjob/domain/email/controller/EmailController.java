package com.goodjob.domain.email.controller;

import com.goodjob.domain.email.service.EmailVerificationService;
import com.goodjob.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/email")
@Slf4j
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
        log.info("들어옴");
        return emailVerificationService.verify(verificationCode);
    }
}
