package com.goodjob.domain.member.controller;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.dto.request.LoginRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import com.goodjob.global.base.rq.Rq;
import com.goodjob.global.base.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final Rq rq;
    private final MemberService memberService;

    @GetMapping("/join")
    public String showJoinForm() {
        return "/member/join";
    }

    @PostMapping("/join")
    public String join(@Valid JoinRequestDto joinRequestDto) {
        RsData<Member> joinRsData = memberService.join(joinRequestDto);

        if (joinRsData.isFail()) {
            return rq.historyBack(joinRsData);
        }

        return rq.redirectWithMsg("/member/login", joinRsData);
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequestDto loginRequestDto) {
        RsData loginRsData = memberService.genAccessToken(loginRequestDto.getAccount(), loginRequestDto.getPassword());

        if (loginRsData.isFail()) {
            return rq.historyBack(loginRsData);
        }

        return rq.redirectWithMsg("/home/index", loginRsData);
    }
}
