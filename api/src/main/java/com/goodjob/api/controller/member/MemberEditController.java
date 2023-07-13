package com.goodjob.api.controller.member;

import com.goodjob.core.domain.member.dto.request.EditRequestDto;
import com.goodjob.core.domain.member.service.MemberEditService;
import com.goodjob.common.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/edit")
public class MemberEditController {

    private final MemberEditService memberEditService;
    private final Rq rq;

    @GetMapping("")
    public String showEdit(EditRequestDto editRequestDto) {
        return "member/edit";
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public String edit(@Valid EditRequestDto editRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/edit";
        }

        RsData updateRsData = memberEditService.update(rq.getMember(), editRequestDto);

        if (updateRsData.isFail()) {
            return rq.historyBack(updateRsData);
        }

        return rq.redirectWithMsg("/member/me", updateRsData);
    }

    @GetMapping("/confirm/password")
    public String showConfirmPassword() {
        return "member/confirm-password";
    }

    @PostMapping("/confirm/password")
    public String confirmPassword(String passwordToEdit) {
        String memberPassword = rq.getMember().getPassword();
        RsData<String> verifyRsData = memberEditService.verifyPassword(passwordToEdit, memberPassword);

        if (verifyRsData.isFail()) {
            return rq.redirectWithMsg("/member/me", verifyRsData);
        }

        return rq.redirectWithMsg("/member/edit", verifyRsData);
    }
}
