package com.goodjob.core.domain.member.service;

import com.goodjob.core.domain.member.dto.request.EditRequestDto;
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

import static com.goodjob.core.domain.member.constant.Membership.FREE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Spy
    private PasswordEncoder passwordEncoder;

    @Spy
    private JwtProvider jwtProvider;

    @Mock
    private MemberRepository memberRepository;

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
                .providerType("GOODJOB")
                .build();
    }

    private static LoginRequestDto getLoginRequestDto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("test");
        loginRequestDto.setPassword("1234");

        return loginRequestDto;
    }

    private static EditRequestDto getEditRequestDto(String tester, String password) {
        EditRequestDto editRequestDto = EditRequestDto.builder()
                .nickname(tester)
                .password(password)
                .build();
        return editRequestDto;
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
    @DisplayName("일반 회원가입 실패 - 중복검증")
    void joinFailDueToDuplicateUsername() {
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
        JoinRequestDto joinRequestDto = getJoinRequestDto();
        Member member = getMember();

        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjEiLCJ1c2VybmFtZSI6InRlc3QiLCJuaWNrbmFtZSI6InRlc3RlciJ9.wqj9OeajmoLQrByZbBbMffPfJOzQgDOjzmzdbaYrZoE";
        Map<String, String> tokens = new HashMap<>() {{
            put("accessToken", jwtToken);
            put("refreshToken", jwtToken);
        }};

        doReturn(Optional.of(member)).when(memberRepository).findByUsername(joinRequestDto.getUsername());
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
    @DisplayName("회원정보 수정 요청 시 비밀번호 일치")
    void verifyPasswordSuccess() {
        // GIVEN
        Member member = getMember();

        doReturn(true).when(passwordEncoder).matches(any(String.class), any(String.class));

        // WHEN
        RsData<String> rsData = memberService.verifyPassword("1234", member.getPassword());

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("비밀번호가 일치합니다. 회원정보수정 페이지로 이동합니다.");
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 요청 시 비밀번호 불일치")
    void verifyPasswordFail() {
        // GIVEN
        Member member = getMember();

        doReturn(false).when(passwordEncoder).matches(any(String.class), any(String.class));

        // WHEN
        RsData<String> rsData = memberService.verifyPassword("1234", member.getPassword());

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("비밀번호가 일치하지않습니다.");
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 성공 - 일반회원 - 닉네임, 비밀번호 변경")
    void updateNicknameAndPasswordSuccess() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("edit Tester", "12345");
        Member member = getMember();

        doReturn(Optional.empty()).when(memberRepository).findByNickname(any(String.class));
        doReturn(false).when(passwordEncoder).matches(any(String.class), any(String.class));
        doReturn("12345").when(passwordEncoder).encode(any(String.class));

        // WHEN
        RsData rsData = memberService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("회원 정보가 수정되었습니다.");
        verify(memberRepository, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 성공 - 일반회원 - 비밀번호변경")
    void updatePasswordSuccess() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("tester", "12345");
        Member member = getMember();

        doReturn(Optional.of(member)).when(memberRepository).findByNickname(any(String.class));
        doReturn(false).when(passwordEncoder).matches(any(String.class), any(String.class));
        doReturn("12345").when(passwordEncoder).encode(any(String.class));

        // WHEN
        RsData rsData = memberService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("회원 정보가 수정되었습니다.");
        verify(memberRepository, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 성공 - 일반회원 - 닉네임변경")
    void updateNicknameSuccess() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("edit Tester", "1234");
        Member member = getMember();

        doReturn(Optional.empty()).when(memberRepository).findByNickname(any(String.class));
        doReturn(true).when(passwordEncoder).matches(any(String.class), any(String.class));
        doReturn("1234").when(passwordEncoder).encode(any(String.class));

        // WHEN
        RsData rsData = memberService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("회원 정보가 수정되었습니다.");
        verify(memberRepository, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 성공 - 소셜회원 - 닉네임변경")
    void updateSocialNicknameSuccess() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("edit Tester", "");
        Member member = Member.builder()
                .password("")
                .nickname("tester")
                .providerType("KAKAO")
                .build();

        doReturn(Optional.empty()).when(memberRepository).findByNickname(any(String.class));

        // WHEN
        RsData rsData = memberService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("회원 정보가 수정되었습니다.");
        verify(memberRepository, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(0)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 일반회원 - 수정된 값 없음")
    void updateFailDueToNoChanges() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("tester", "1234");
        Member member = getMember();

        doReturn(Optional.of(member)).when(memberRepository).findByNickname(any(String.class));
        doReturn(true).when(passwordEncoder).matches(any(String.class), any(String.class));

        // WHEN
        RsData rsData = memberService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("수정된 정보가 없습니다!");
        verify(memberRepository, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 소셜회원 - 수정된 값 없음")
    void updateSocialFailDueToNoChanges() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("tester", "");
        Member member = Member.builder()
                .password("")
                .nickname("tester")
                .providerType("KAKAO")
                .build();

        doReturn(Optional.of(member)).when(memberRepository).findByNickname(any(String.class));

        // WHEN
        RsData rsData = memberService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("수정된 정보가 없습니다!");
        verify(memberRepository, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(0)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 전체회원 - 중복된 닉네임")
    void updateFailDueToDuplicateNickname() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("test", "1234");
        Member member = getMember();

        doReturn(Optional.of(member)).when(memberRepository).findByNickname(any(String.class));

        // WHEN
        RsData rsData = memberService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("이미 존재하는 닉네임 입니다.");
        verify(memberRepository, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(0)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }
}