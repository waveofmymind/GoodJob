package com.goodjob.global.base.security;

import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    // 요청이 제일 먼저 도달하는 지점
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauthId = oAuth2User.getName();
        String providerType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String username = providerType + "%s".formatted(oauthId);

        String email = null;

        Map<String, Object> userAttributes = oAuth2User.getAttributes();

        if (userAttributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) userAttributes.get("kakao_account");

            if (kakaoAccount.containsKey("email")) {
                email = (String) kakaoAccount.get("email");
            }
        }

        log.info("oAuth2User ={}", oAuth2User);
        log.info("email ={}", email);
        Member member = memberService.whenSocialLogin(providerType, username, email).getData();

        return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getAuthorities());
    }
}
