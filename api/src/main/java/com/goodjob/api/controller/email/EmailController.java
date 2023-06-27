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
    public void sendEmail(String email) {
        emailVerificationService.send(email);
    }

    @PostMapping("/verify")
    @ResponseBody
    public RsData verifyEmail(String verificationCode) {
        return emailVerificationService.verify(verificationCode);
    }
}
