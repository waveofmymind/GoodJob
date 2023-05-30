package com.goodjob.domain.member.controller;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.dto.request.LoginRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import com.goodjob.global.base.rq.Rq;
import com.goodjob.global.base.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {
    private final Rq rq;
    private final MemberService memberService;

    @GetMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String showJoin() {
        return "/member/join";
    }

    @PostMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String join(@Valid JoinRequestDto joinRequestDto) {
        RsData<Member> joinRsData = memberService.join(joinRequestDto);

        if (joinRsData.isFail()) {
            return rq.historyBack(joinRsData);
        }

        return rq.redirectWithMsg("/member/login", joinRsData);
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String showLogin() {
        log.info("get login 요청 받음!");

        // TODO: 추후 경로 수정
        return "/member/login";
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(@Valid LoginRequestDto loginRequestDto) {
        log.info("loginRequestDto= {}", loginRequestDto.toString());

        RsData loginRsData = memberService.genAccessToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        log.info("loginRsData.ResultCode ={}", loginRsData.getResultCode());
        log.info("loginRsData.Data ={}", loginRsData.getData());

        if (loginRsData.isFail()) {
            // TODO: 추후 경로 수정
            return "/member/join";
        }

        // TODO: 추후 경로 수정
        return "/member/join";
    }

    // TODO: 삭제안됨.. 추후 수정
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        log.info("id ={}", id);
        memberService.delete(id);
        // TODO: 추후 경로 수정
        return "/member/join";
    }
}
