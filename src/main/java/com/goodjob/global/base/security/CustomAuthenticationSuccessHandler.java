package com.goodjob.global.base.security;

import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import com.goodjob.global.base.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Member member = memberService.findByUsername(user.getUsername()).orElse(null);
        String jwtToken = jwtProvider.genToken(member.toClaims());

        // jwt 토큰을 Authorization 헤더에 설정
        response.setHeader("Authorization", "Bearer " + jwtToken);
        response.sendRedirect("/");
    }
}
