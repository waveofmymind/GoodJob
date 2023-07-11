package com.goodjob.core.domain.member.service;

import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.dto.request.LoginRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.repository.MemberRepository;
import com.goodjob.core.global.base.jwt.JwtProvider;
import com.goodjob.core.global.base.rsData.RsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.goodjob.core.domain.member.constant.Membership.*;
import static com.goodjob.core.domain.member.constant.ProviderType.GOODJOB;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Spy
    private JwtProvider jwtProvider;

    @InjectMocks
    private MemberService memberService;

    private static JoinRequestDto getJoinRequestDto() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("test");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setConfirmPassword("1234");
        joinRequestDto.setNickname("tester");
        joinRequestDto.setEmail("test@naver.com");

        return joinRequestDto;
    }

    private static Member getMember() {
        // 초기 회원 설정
        return Member.builder()
                .id(1L)
                .username("test")
                .password("1234")
                .nickname("tester")
                .email("test@naver.com")
                .membership(FREE)
                .coin(10)
                .providerType(GOODJOB)
                .build();
    }

    private static LoginRequestDto getLoginRequestDto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("test");
        loginRequestDto.setPassword("1234");

        return loginRequestDto;
    }

    @Test
    @DisplayName("일반 회원가입 성공")
    void joinSuccess() {
        // GIVEN
        JoinRequestDto joinRequestDto = getJoinRequestDto();

        // WHEN
        RsData<Member> rsData = memberService.join(joinRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("%s님의 회원가입이 완료되었습니다.".formatted(joinRequestDto.getNickname()));
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("일반 회원가입 실패 - 아이디 중복")
    void joinFail_DuplicateUsername() {
        // GIVEN
        JoinRequestDto joinRequestDto = getJoinRequestDto();
        Member member = getMember();

        doReturn(Optional.of(member)).when(memberRepository).findByUsername(joinRequestDto.getUsername());

        // WHEN
        RsData<Member> rsData = memberService.join(joinRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("이미 존재하는 계정입니다.");
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }

    @Test
    @DisplayName("일반 회원가입 실패 - 닉네임 중복")
    void joinFail_DuplicateNickname() {
        // GIVEN
        JoinRequestDto joinRequestDto = getJoinRequestDto();
        joinRequestDto.setUsername("notDuplication");
        Member member = getMember();

        doReturn(Optional.of(member)).when(memberRepository).findByNickname(joinRequestDto.getNickname());

        // WHEN
        RsData<Member> rsData = memberService.join(joinRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("이미 존재하는 닉네임입니다.");
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }

    @Test
    @DisplayName("소셜 회원가입 성공")
    void socialJoinSuccess() {
        // WHEN
        RsData<Member> rsData = memberService.socialJoin("KAKAO", "KAKAO5413639824", "", null);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("%s님의 회원가입이 완료되었습니다.".formatted(rsData.getData().getNickname()));
        verify(passwordEncoder, times(0)).encode(any(String.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        // GIVEN
        LoginRequestDto loginRequestDto = getLoginRequestDto();
        Member member = getMember();

        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjEiLCJ1c2VybmFtZSI6InRlc3QiLCJuaWNrbmFtZSI6InRlc3RlciJ9.wqj9OeajmoLQrByZbBbMffPfJOzQgDOjzmzdbaYrZoE";
        Map<String, String> tokens = new HashMap<>() {{
            put("accessToken", jwtToken);
            put("refreshToken", jwtToken);
        }};

        doReturn(Optional.of(member)).when(memberRepository).findByUsername(loginRequestDto.getUsername());
        doReturn(true).when(passwordEncoder).matches(any(String.class), any(String.class));
        doReturn(tokens).when(jwtProvider).genAccessTokenAndRefreshToken(any(Member.class));

        // WHEN
        RsData<Map<String, String>> rsData = memberService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("%s님 환영합니다!".formatted(member.getNickname()));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("로그인 실패 - 아이디 틀림")
    void loginFail_InvalidUsername() {
        // GIVEN
        LoginRequestDto loginRequestDto = getLoginRequestDto();
        Member member = getMember();

        doReturn(Optional.empty()).when(memberRepository).findByUsername(loginRequestDto.getUsername());

        // WHEN
        RsData<Map<String, String>> rsData = memberService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("아이디 혹은 비밀번호가 틀립니다.");
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void loginFail_InvalidPassword() {
        // GIVEN
        LoginRequestDto loginRequestDto = getLoginRequestDto();
        Member member = getMember();

        doReturn(Optional.of(member)).when(memberRepository).findByUsername(loginRequestDto.getUsername());
        doReturn(false).when(passwordEncoder).matches(any(String.class), any(String.class));

        // WHEN
        RsData<Map<String, String>> rsData = memberService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("아이디 혹은 비밀번호가 틀립니다.");
    }

    @Test
    @DisplayName("프리미엄 등급 업그레이드 성공")
    void upgradeSuccess_PremiumMembership() {
        // GIVEN
        Member member = getMember();

        // WHEN
        memberService.upgradeToPremiumMembership(member);

        // THEN
        assertThat(member.getMembership()).isEqualTo(PREMIUM);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("멘토 등급 업그레이드 성공")
    void upgradeSuccess_MentorMembership() {
        // GIVEN
        Member member = getMember();

        // WHEN
        memberService.upgradeToMentorMembership(member);

        // THEN
        assertThat(member.getMembership()).isEqualTo(MENTOR);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("코인 감소 성공")
    void deductCoinSuccess() {
        // GIVEN
        Member member = getMember();

        // WHEN
        memberService.deductCoin(member);

        // THEN
        assertThat(member.getCoin()).isEqualTo(9);
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}