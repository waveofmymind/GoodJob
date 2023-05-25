package com.goodjob.domain.member.controller;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.dto.request.LoginRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    public String showJoinForm() {
        return "/member/join";
    }

    @PostMapping("/join")
    public Member join(JoinRequestDto joinRequestDto) {
        Member member = memberService.join(joinRequestDto);

        return member;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String login(LoginRequestDto loginRequestDto, Model model) {
        Member member = memberService.findByAccount(loginRequestDto.getAccount()).orElse(null);

        if (member == null) {
            return "F-1, 실패 메시지: 아이디 혹은 비밀번호가 틀립니다.";
        }

        model.addAttribute("member", member);

        return "S-1, redirect: 메인화면";
    }
}
