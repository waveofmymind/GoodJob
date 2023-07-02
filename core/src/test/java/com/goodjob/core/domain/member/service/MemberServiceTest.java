package com.goodjob.core.domain.member.service;

import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.repository.MemberRepository;
import com.goodjob.core.global.base.jwt.JwtProvider;
import com.goodjob.core.global.base.rsData.RsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("일반 회원가입 성공")
    void joinSuccess() {
        // GIVEN
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("test");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setConfirmPassword("1234");
        joinRequestDto.setNickname("tester");
        joinRequestDto.setEmail("test@naver.com");

        // WHEN
        RsData<Member> rsData = memberService.join(joinRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getData().getUsername()).isEqualTo("test");
        assertThat(rsData.getData().getUserRole()).isEqualTo("free");
    }
}