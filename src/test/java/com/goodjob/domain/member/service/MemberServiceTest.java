package com.goodjob.domain.member.service;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.global.base.rsData.RsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입")
    void t01() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("tester1");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setNickname("tester1");
        joinRequestDto.setEmail("tester1@test.com");

        RsData<Member> rsData = memberService.join(joinRequestDto);
        Member foundMember = memberService.findByUsername("tester1").get();

        assertThat(rsData.getResultCode().startsWith("S-")).isTrue();
        assertThat(foundMember.getCreatedDate()).isNotNull();
        assertThat(passwordEncoder.matches("1234", foundMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("로그인")
    void t02() {

    }
}