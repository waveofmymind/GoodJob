package com.goodjob.global.base.cookie;

import com.goodjob.global.base.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CookieUt {
    public Cookie createCookie(String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (JwtProvider.TOKEN_VALIDATION_SECOND / 1000L));
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
