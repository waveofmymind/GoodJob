package com.goodjob.api.controller.member;

import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/recover")
public class MemberRecoverController {

    private final MemberService memberService;

    @GetMapping("/username")
    public String showRecoverUsername() {
        return "member/recover-username";
    }

    @PostMapping("/username")
    @ResponseBody
    public RsData<String> recoverUsername(String email) {
        Optional<Member> opMember = memberService.findByEmail(email);

        if (opMember.isEmpty()) {
            return RsData.of("F-1", "해당 이메일을 가진 회원이 없습니다.");
        }

        String nickname = opMember.get().getNickname();
        String filteredNickname = nickname.substring(0, nickname.length() - 4) + "****";

        return RsData.of("S-1", filteredNickname);
    }

    @PostMapping("/password")
    @PreAuthorize("isAnonymous()")
    public String recoverPassword() {
        return null;
    }
}
