package com.goodjob.core.global.base.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CookieUt {

    private final static int COOKIE_MAX_AGE_SECOND = 60 * 30;
    private final static int SUB_COOKIE_MAX_AGE_SECOND = 60 * 60 * 24 * 14;

    public Cookie createCookie(String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(COOKIE_MAX_AGE_SECOND);
        cookie.setPath("/");

        return cookie;
    }

    public Cookie createRefreshCookie(String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(SUB_COOKIE_MAX_AGE_SECOND);
        cookie.setPath("/");

        return cookie;
    }

    public Cookie getCookie(HttpServletRequest req, String cookieName) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length > 0) {
            return Arrays.stream(cookies)
                    .filter(e -> e.getName().equals(cookieName))
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    public Cookie expireCookie(Cookie cookie) {
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
