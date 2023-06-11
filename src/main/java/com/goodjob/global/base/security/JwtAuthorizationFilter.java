package com.goodjob.global.base.security;

import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import com.goodjob.global.base.jwt.JwtProvider;
import com.goodjob.global.base.cookie.CookieUt;
import com.goodjob.global.base.redis.RedisUt;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.Optional;

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
        // 쿠키에서 accessToken 값을 가져온다.
        Cookie accessToken = cookieUt.getCookie(request, "accessToken");

        if (accessToken != null) {
            String token = accessToken.getValue();
            log.info("token = {}", token);

            try {
                if (jwtProvider.verify(token)) {
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    long id = (int) claims.get("id");

                    Optional<Member> opMember = memberService.findById(id);

                    if (opMember.isPresent()) {
                        forceAuthentication(opMember.get());
                    }
                }
            } catch (ExpiredJwtException e) {
                Map<String, Object> claims = jwtProvider.getClaims(token);
                // TODO: 리팩토링시 id를 키값에 넣는걸로
                String username = (String) claims.get("username");
                Long ttl = redisUt.getExpire(username);

                if (ttl == -1) { // 키가 존재하지 않는 경우
                    log.info("존재하지 않는 refreshToken key!");
                }

                if (ttl == -2) { // 리프레시 토큰 만료된 경우
                    // TODO: redirect
                    log.info("만료된 refreshToken. 재로그인 필요!");
                }

                // 새로운 액세스 토큰 발급
                Member member = memberService.findByUsername(username).orElseThrow();
                String newAccessToken = jwtProvider.genToken(member.toClaims());
                log.info("새로 발급된 accessToken ={}", newAccessToken);

                response.addCookie(cookieUt.createCookie("accessToken", newAccessToken));
                response.addCookie(cookieUt.createCookie("id", username));
            }
        }

        filterChain.doFilter(request, response);
    }

    // 강제로 로그인 처리하는 메서드 (로그인한 사용자의 정보를 가져옴)
    private void forceAuthentication(Member member) {
        User user = new User(member.getUsername(), member.getPassword(), member.getAuthorities());
//        log.info("user= {}", user);
        // 스프링 시큐리티 객체에 저장할 authentication 객체를 생성
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                user,
                null,
                member.getAuthorities()
        );

        // 스프링 시큐리티 내에 우리가 만든 authentication 객체를 저장할 context 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // context 에 유저정보 저장
        context.setAuthentication(authentication);
        // 스프링 시큐리티에 context 등록
        SecurityContextHolder.setContext(context);
    }
}
