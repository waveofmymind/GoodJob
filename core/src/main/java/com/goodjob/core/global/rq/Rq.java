package com.goodjob.core.global.rq;


import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.global.base.cookie.CookieUt;
import com.goodjob.core.global.base.jwt.JwtProvider;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.global.util.Ut;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
@RequestScope
@Slf4j
public class Rq {
    private final JwtProvider jwtProvider;
    private final CookieUt cookieUt;
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final User user;
    private Member member = null;

    public Rq(JwtProvider jwtProvider, CookieUt cookieUt, MemberService memberService, HttpServletRequest req, HttpServletResponse resp) {
        this.jwtProvider = jwtProvider;
        this.cookieUt = cookieUt;
        this.memberService = memberService;
        this.req = req;
        this.resp = resp;

        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();

        if (principal instanceof User) {
            this.user = (User) principal;
        } else {
            this.user = null;
        }
    }

    // 로그인 되어 있는지 체크
    public boolean isLogin() {
        return user != null;
    }

    // 로그아웃 되어 있는지 체크
    public boolean isLogout() {
        return !isLogin();
    }

    // 로그인 된 회원의 객체
    public Member getMember() {
        if (isLogout()) return null;

        // 데이터가 없는지 체크
        if (member == null) {
            member = memberService.findByUsername(user.getUsername()).orElseThrow();
        }

        return member;
    }

    // 뒤로가기 + 메세지
    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg);
        // 200 이 아니라 400 으로 응답코드가 지정되도록
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "common/js";
    }

    // 뒤로가기 + 메세지
    public String historyBack(RsData rsData) {
        return historyBack(rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, RsData rsData) {
        return redirectWithMsg(url, rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }

    private String urlWithMsg(String url, String msg) {
        // 기존 URL에 혹시 msg 파라미터가 있다면 그것을 지우고 새로 넣는다.
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }

    // 메세지에 ttl 적용
    private String msgWithTtl(String msg) {
        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }

    public String getParamsJsonStr() {
        Map<String, String[]> parameterMap = req.getParameterMap();

        return Ut.json.toStr(parameterMap);
    }

    public Cookie getCookie(String cookieName) {
        return cookieUt.getCookie(req, cookieName);
    }

    public void setCookie(String cookieName, String value) {
        if (!value.contains("/member/join")) {
            Cookie cookie = cookieUt.createCookie(cookieName, value);
            resp.addCookie(cookie);
        }
    }

    public void expireCookie(String cookieName) {
        Cookie cookie = getCookie(cookieName);
        cookie = cookieUt.expireCookie(cookie);
        resp.addCookie(cookie);
    }

    public void setJwtTokenToOAuth2User(Authentication authentication) {
        // 유저에게 jwt 토큰 발급
        User user = (User) authentication.getPrincipal();
        Optional<Member> opMember = memberService.findByUsername(user.getUsername());

        if (opMember.isPresent()) {
            String token = jwtProvider.genToken(opMember.get().toClaims());

            // jwt 토큰을 쿠키에 설정
            setCookie("accessToken", token);
        }
    }

    public String getReferer() {
        String referer = req.getHeader("referer");

        if (referer != null) {
            int queryIndex = referer.indexOf("?");

            if (queryIndex != -1) {
                referer = referer.substring(0, queryIndex);
            }
        }

        return referer;
    }

    public String getPreviousUrl(Cookie cookie) {
        String preUrl = cookie.getValue();
        expireCookie(cookie.getName());

        return preUrl;
    }

    public void oath2logout() {
        // logoutUrl로 온 요청인지 확인 -> 맞으면 SecurityContext에서 인증객체 꺼내서
        SecurityContext context = SecurityContextHolder.getContext();

        // SecurityContenxtLogoutHandler에 인증 객체 전달.
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(req, resp, context.getAuthentication());
    }
}