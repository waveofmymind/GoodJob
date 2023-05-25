package com.goodjob.domain.member.controller;

import com.goodjob.domain.member.dto.request.MemberRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public Member join(MemberRequestDto memberRequestDto) {
        Member member = memberService.join(memberRequestDto);

        return member;
    }

    @GetMapping("/list")
    public List<Member> list() {

    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String login(MemberRequestDto memberRequestDto) {
        // TODO: 로그인기능
        return null;
    }
}
