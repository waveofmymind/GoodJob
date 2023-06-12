package com.goodjob.global.config;

import com.goodjob.global.base.jwt.JwtAccessDeniedHandler;
import com.goodjob.global.base.jwt.JwtAuthenticationEntryPoint;
import com.goodjob.global.base.security.JwtAuthorizationFilter;
import com.goodjob.global.base.security.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //TODO: 추후 게시글 작성 경로 권한 추가 필요
        http.csrf(AbstractHttpConfigurer::disable).
                sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/article/create", "/article/update/**", "article/delete/**", "/comment/create/**", "/comment/update/**", "comment/delete/**", "/subComment/create/**", "/subComment/update/**", "/subComment/delete/**", "/likes/like/article/**", "/likes/like/comment/**", "/likes/like/subComment/**").hasAuthority("USER")
                        .requestMatchers("/**", "/resumes/**", "/member/**", "/article/**", "/jobstatistic/**").permitAll()
                )
                .httpBasic(httpBasicConfigurer -> httpBasicConfigurer.disable())
                // TODO: 받아오는거까진 됨 -> 필터 거치면서 쿠키저장하도록
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/member/login")
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .addFilterBefore(
                        jwtAuthorizationFilter, // 액세스 토큰으로부터 로그인 처리
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/**","/templates/**");
    }
}
