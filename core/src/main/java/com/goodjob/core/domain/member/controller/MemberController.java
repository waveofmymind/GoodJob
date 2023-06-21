package com.goodjob.core.domain.member.controller;

import com.goodjob.core.domain.member.dto.request.EditRequestDto;
import com.goodjob.core.global.base.redis.RedisUt;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.dto.request.LoginRequestDto;
import com.goodjob.core.domain.member.service.MemberService;

import com.goodjob.core.global.rq.Rq;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final Rq rq;
    private final MemberService memberService;
    private final RedisUt redisUt;

    @GetMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String showJoin(JoinRequestDto joinRequestDto) {
        return "member/join";
    }

    @PostMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String join(@Valid JoinRequestDto joinRequestDto,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        if (!joinRequestDto.getPassword().equals(joinRequestDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "", "패스워드가 일치하지 않습니다.");
            return "member/join";
        }

        RsData<Member> joinRsData = memberService.join(joinRequestDto);
        if (joinRsData.isFail()) {
            return rq.historyBack(joinRsData);
        }

        return rq.redirectWithMsg("/member/login", joinRsData);
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String showLogin() {
        String referer = rq.getReferer();
        rq.setCookie("previousUrl", referer);

        return "member/login";
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(@Valid LoginRequestDto loginRequestDto) {
        RsData loginRsData = memberService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        if (loginRsData.isFail()) {
            return rq.historyBack(loginRsData);
        }

        rq.setCookie("accessToken", (String) loginRsData.getData());

        Cookie previousUrlCookie = rq.getCookie("previousUrl");

        if (previousUrlCookie != null) {
            String previousUrl = rq.getPreviousUrl(previousUrlCookie);

            return rq.redirectWithMsg(previousUrl, loginRsData);
        }

        return rq.redirectWithMsg("/", loginRsData);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public String logout() {
        String username = rq.getMember().getUsername();
        // 레디스에서 리프레시토큰삭제
        redisUt.deleteToken(username);
        // 쿠키삭제
        rq.expireCookie("accessToken");

        return rq.redirectWithMsg("/", "로그아웃 되었습니다.");
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String showMe() {
        return "member/me";
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public String showEdit() {
        return "member/edit";
    }

    @PatchMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Long id, EditRequestDto editRequestDto,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/edit";
        }

        RsData updateRsData = memberService.update(rq.getMember(), editRequestDto);

        if (updateRsData.isFail()) {
            rq.historyBack(updateRsData);
        }

        return rq.redirectWithMsg("/member/me", updateRsData);
    }

    @PostMapping("/edit/confirm/password")
    @ResponseBody
    public String confirmPassword(String password) {
        String memberPassword = rq.getMember().getPassword();
        boolean isMatched = memberService.matchPassword(password, memberPassword);

        // TODO: 추후 로직 리팩토링
        if (!isMatched) {
            return "member/confirm-password";
        }

        return "redirect:/member/edit";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);

        return "member/join";
    }

    @PostMapping("/join/valid/username")
    @ResponseBody
    public RsData<String> checkDuplicateUsername(String username) {
        if (username.length() < 4) {
            return RsData.of("F-1", "4자 이상 입력하세요.");
        }

        Optional<Member> opMember = memberService.findByUsername(username);
        if (opMember.isPresent()) {
            return RsData.of("F-1", "이미 사용중인 아이디입니다.");
        }

        return RsData.of("S-1", "사용 가능한 아이디입니다.");
    }

    @PostMapping("/join/valid/nickname")
    @ResponseBody
    public RsData checkDuplicateNickname(String nickname) {
        if (nickname.length() < 2) {
            return RsData.of("F-1", "2자 이상 입력하세요.");
        }

        Optional<Member> opMember = memberService.findByNickName(nickname);
        if (opMember.isPresent()) {
            return RsData.of("F-1", "이미 사용중인 닉네임입니다.");
        }

        return RsData.of("S-1", "사용 가능한 닉네임입니다.");
    }
}
