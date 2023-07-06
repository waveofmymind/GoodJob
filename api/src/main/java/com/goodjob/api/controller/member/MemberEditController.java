package com.goodjob.api.controller.member;

import com.goodjob.core.domain.member.dto.request.EditRequestDto;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/edit")
public class MemberEditController {

    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/")
    public String showEdit(EditRequestDto editRequestDto) {
        return "member/edit";
    }

    @PatchMapping("/")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public RsData<String> edit(EditRequestDto editRequestDto) {
        return memberService.update(rq.getMember(), editRequestDto);
    }

    @GetMapping("/confirm/password")
    public String showConfirmPassword() {
        return "member/confirm-password";
    }

    @PostMapping("/confirm/password")
    public String confirmPassword(String passwordToEdit) {
        String memberPassword = rq.getMember().getPassword();
        RsData<String> matchRsData = memberService.matchPassword(passwordToEdit, memberPassword);

        if (matchRsData.isFail()) {
            return rq.redirectWithMsg("/member/me", matchRsData);
        }

        return "redirect:/member/edit";
    }
}
