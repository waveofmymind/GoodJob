package com.goodjob.core.global.security;



import com.goodjob.core.global.base.cookie.CookieUt;
import com.goodjob.core.global.base.redis.RedisUt;
import com.goodjob.core.global.base.jwt.JwtProvider;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final CookieUt cookieUt;
    private final RedisUt redisUt;
    private Optional<Member> opMember;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 쿠키에서 accessToken 값을 가져온다.
        Cookie accessToken = cookieUt.getCookie(request, "accessToken");

        if (accessToken != null) {
            String token = accessToken.getValue();

            try {
                if (jwtProvider.verify(token)) {
                    Map<String, Object> claims = jwtProvider.getClaims(token);
                    long id = (int) claims.get("id");

                    opMember = memberService.findById(id);

                    if (opMember.isPresent()) {
                        forceAuthentication(opMember.get());
                    }
                }
            } catch (ExpiredJwtException e) {
                String userId = String.valueOf(opMember.get().getId());
                Long ttl = redisUt.getExpire(userId);

                if (ttl < 0) { // 리프레시 토큰까지 만료되었거나 키가 존재하지 않는 경우
                    // 재로그인
                    response.sendRedirect("/member/login");
                }

                // 새로운 액세스 토큰 발급
                String newAccessToken = jwtProvider.genToken(opMember.get().toClaims());

                response.addCookie(cookieUt.createCookie("accessToken", newAccessToken));
            }
        }

        filterChain.doFilter(request, response);
    }

    // 강제로 로그인 처리하는 메서드 (로그인한 사용자의 정보를 가져옴)
    private void forceAuthentication(Member member) {
        User user = new User(member.getUsername(), member.getPassword(), member.getAuthorities());
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
