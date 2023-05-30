package com.goodjob.global.base.jwt;

import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 Authorization 값을 가져온다.
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            String token = bearerToken.substring("Bearer".length());

            // jwt 토큰이 유효하다면
            if (jwtProvider.verify(token)) {
                Map<String, Object> claims = jwtProvider.getClaims(token);
                String username = (String) claims.get("username");

                Member member = memberService.findByUsername(username).orElseThrow(() ->
                        new UsernameNotFoundException(username));

                forceAuthentication(member);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 강제로 로그인 처리하는 메소드
    private void forceAuthentication(Member member) {
        User user = new User(member.getUsername(), member.getPassword(), member.getAuthorities());

        // 스프링 시큐리티 객체에 저장할 authentication 객체 생성
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                user,
                null,
                member.getAuthorities()
        );

        // 스프링 시큐리티 내에 우리가 만든 authentication 객체를 저장할 context 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
