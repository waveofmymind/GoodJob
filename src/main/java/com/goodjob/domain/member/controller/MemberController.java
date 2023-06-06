package com.goodjob.domain.member.controller;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.dto.request.LoginRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import com.goodjob.global.base.redis.RedisUt;
import com.goodjob.global.base.rq.Rq;
import com.goodjob.global.base.rsData.RsData;
import com.goodjob.global.base.cookie.CookieUt;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final Rq rq;
    private final MemberService memberService;
    private final CookieUt cookieUt;
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
        log.debug("joinRequestDto ={}", joinRequestDto);

        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        if (!joinRequestDto.getPassword().equals(joinRequestDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "", "패스워드가 일치하지 않습니다.");
            return "member/join";
        }

        RsData<Member> joinRsData = memberService.join(joinRequestDto);
        log.debug("joinRsData ={}", joinRsData.toString());
        if (joinRsData.isFail()) {
            return rq.historyBack(joinRsData);
        }

        return rq.redirectWithMsg("/member/login", joinRsData);
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String showLogin() {
        return "member/login";
    }
    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(@Valid LoginRequestDto loginRequestDto,
                        BindingResult bindingResult) {
        log.debug("loginRequestDto= {}", loginRequestDto.toString());

        if (bindingResult.hasErrors()) {
            return "member/login";
        }

        RsData loginRsData = memberService.genAccessToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        log.debug("loginRsData ={}", loginRsData);

        if (loginRsData.isFail()) {
            return rq.historyBack(loginRsData);
        }

        // TODO: 리팩토링
        String data = (String) loginRsData.getData();
        Cookie accessTokenCookie = cookieUt.createCookie("accessToken", data);
        Cookie usernameCookie = cookieUt.createCookie("username", loginRequestDto.getUsername());

        rq.setCookie(accessTokenCookie);
        rq.setCookie(usernameCookie);

        return "redirect:/";
    }

    // TODO: 삭제안됨.. 추후 수정
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id) {
        log.debug("id ={}", id);
        memberService.delete(id);

        return "member/join";
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public String logout() {
        String username = rq.getMember().getUsername();
        // TODO: 리팩토링
        // 레디스에서 리프레시토큰삭제
        redisUt.deleteToken(username);
        // 쿠키에서 jwt토큰, username 제거.
        Cookie accessTokenCookie = rq.getCookie("accessToken");
        Cookie usernameCookie = rq.getCookie("username");

        accessTokenCookie = cookieUt.expireCookie(accessTokenCookie);
        usernameCookie = cookieUt.expireCookie(usernameCookie);
        // 로그레벨조정 debug.

        rq.setCookie(accessTokenCookie);
        rq.setCookie(usernameCookie);
        return rq.redirectWithMsg("/", "로그아웃 되었습니다.");
    }

    @PostMapping("/join/valid/username")
    @ResponseBody
    public RsData<String> checkDuplicateUsername(String username) {
        Optional<Member> opMember = memberService.findByUsername(username);
        if (opMember.isPresent()) {
            return RsData.of("F-1", "이미 사용중인 아이디입니다.");
        }

        return RsData.of("S-1", "사용 가능한 아이디입니다.");
    }

    @PostMapping("/join/valid/nickname")
    @ResponseBody
    public RsData checkDuplicateNickname(String nickname) {
        Optional<Member> opMember = memberService.findByNickName(nickname);
        if (opMember.isPresent()) {
            return RsData.of("F-1", "이미 사용중인 닉네임입니다.");
        }

        return RsData.of("S-1", "사용 가능한 닉네임입니다.");
    }

    @PostMapping("/join/valid/email")
    @ResponseBody
    public RsData checkDuplicateEmail(String email) {
        Optional<Member> opMember = memberService.findByEmail(email);
        if (opMember.isPresent()) {
            return RsData.of("F-1", "이미 사용중인 이메일입니다.");
        }

        return RsData.of("S-1", "사용 가능한 이메일입니다.");
    }
}
