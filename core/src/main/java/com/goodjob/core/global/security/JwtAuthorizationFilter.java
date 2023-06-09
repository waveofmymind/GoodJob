package com.goodjob.core.global.security;



import com.goodjob.common.cookie.CookieUt;
import com.goodjob.member.entity.Member;
import com.goodjob.member.jwt.JwtProvider;
import com.goodjob.common.redis.RedisUt;
import com.goodjob.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

import static com.goodjob.common.cookie.constant.CookieType.ACCESS_TOKEN;
import static com.goodjob.common.cookie.constant.CookieType.REFRESH_TOKEN;
import static com.goodjob.member.jwt.JwtProvider.ACCESS_TOKEN_VALIDATION_SECOND;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final CookieUt cookieUt;
    private final RedisUt redisUt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie accessToken = cookieUt.getCookie(request, ACCESS_TOKEN.value());

        // accessToken 만료된 경우
        if (accessToken == null) {
            // 리프레시 토큰 확인
            Cookie refreshToken = cookieUt.getCookie(request, REFRESH_TOKEN.value());
            if (refreshToken != null) {
                createNewAccessToken(refreshToken, response);
            }
        } else {
            String token = accessToken.getValue();

            if (jwtProvider.verify(token)) {
                Map<String, Object> claims = jwtProvider.getClaims(token);
                long id = (int) claims.get("id");

                Member member = memberService.findById(id).orElse(null);

                forceAuthentication(member);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 새로운 액세스 토큰 발급하는 메서드
    private void createNewAccessToken(Cookie refreshToken, HttpServletResponse response) throws IOException {
        log.debug("토큰 만료");
        String token = refreshToken.getValue();
        Map<String, Object> claims = jwtProvider.getClaims(token);

        long id = (int) claims.get("id");
        Member member = memberService.findById(id).orElse(null);

        Long ttl = redisUt.getExpire(String.valueOf(id));

        // 리프레시 토큰까지 만료되었거나 키가 존재하지 않는 경우
        if (ttl < 0) {
            log.debug("재로그인");
            response.sendRedirect("/member/login");
        }

        String newAccessToken = jwtProvider.genToken(member.toClaims(), ACCESS_TOKEN_VALIDATION_SECOND);

        response.addCookie(cookieUt.createCookie(ACCESS_TOKEN.value(), newAccessToken));
    }

    // 로그인한 사용자 정보 가져와서 시큐리티에 등록
    private void forceAuthentication(Member member) {
        User user = new User(member.getUsername(), member.getPassword(), member.getAuthorities());
        // 스프링 시큐리티 객체에 저장할 authentication 객체 생성
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(user, null, member.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        // 스프링 시큐리티에 context 등록
        SecurityContextHolder.setContext(context);
    }
}
