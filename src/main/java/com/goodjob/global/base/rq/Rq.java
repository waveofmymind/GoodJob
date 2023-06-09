package com.goodjob.global.base.rq;

import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import com.goodjob.global.base.cookie.CookieUt;
import com.goodjob.global.base.jwt.JwtProvider;
import com.goodjob.global.base.rsData.RsData;
import com.goodjob.global.base.security.CustomDetailsService;
import com.goodjob.global.util.Ut;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;
import java.util.Map;

@Component
@RequestScope
public class Rq {
    private final JwtProvider jwtProvider;
    private final CookieUt cookieUt;
    private final CustomDetailsService customDetailsService;
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final User user;
    private Member member = null;

    public Rq(JwtProvider jwtProvider, CookieUt cookieUt, CustomDetailsService customDetailsService, MemberService memberService, HttpServletRequest req, HttpServletResponse resp) {
        this.cookieUt = cookieUt;
        this.jwtProvider = jwtProvider;
        this.customDetailsService = customDetailsService;
        this.memberService = memberService;
        this.req = req;
        this.resp = resp;

        // jwt 토큰에서 회원의 인증정보를 가져옴
        Cookie accessToken = cookieUt.getCookie(req, "accessToken");
        if (accessToken != null) {
            String token = accessToken.getValue();
            Map<String, Object> claims = jwtProvider.getClaims(token);
            String username = (String) claims.get("username");
            this.user = (User) customDetailsService.loadUserByUsername(username);
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
        Cookie cookie = cookieUt.createCookie(cookieName, value);
        resp.addCookie(cookie);
    }
    public void expireCookie(String cookieName) {
        Cookie cookie = getCookie(cookieName);
        cookie = cookieUt.expireCookie(cookie);
        resp.addCookie(cookie);
    }
}