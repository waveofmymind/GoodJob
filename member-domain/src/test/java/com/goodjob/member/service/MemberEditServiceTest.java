package com.goodjob.member.service;


import com.goodjob.common.rsData.RsData;
import com.goodjob.member.dto.request.EditRequestDto;
import com.goodjob.member.entity.Member;
import com.goodjob.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


import static com.goodjob.member.constant.Membership.FREE;
import static com.goodjob.member.constant.ProviderType.GOODJOB;
import static com.goodjob.member.constant.ProviderType.KAKAO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberEditServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberEditService memberEditService;

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

    private static EditRequestDto getEditRequestDto(String tester, String password) {
        EditRequestDto editRequestDto = EditRequestDto.builder()
                .nickname(tester)
                .password(password)
                .build();
        return editRequestDto;
    }

    @Test
    @DisplayName("회원정보 수정 성공 요청 시 현재 비밀번호 확인 성공")
    void editSuccess_CurrentPasswordMatch() {
        // GIVEN
        Member member = getMember();

        doReturn(true).when(passwordEncoder).matches(any(String.class), any(String.class));

        // WHEN
        RsData<String> rsData = memberEditService.verifyPassword("1234", member.getPassword());

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("비밀번호가 일치합니다. 회원정보수정 페이지로 이동합니다.");
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 요청 시 현재 비밀번호 확인 실패")
    void editFail_CurrentPasswordMismatch() {
        // GIVEN
        Member member = getMember();

        doReturn(false).when(passwordEncoder).matches(any(String.class), any(String.class));

        // WHEN
        RsData<String> rsData = memberEditService.verifyPassword("1234", member.getPassword());

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("비밀번호가 일치하지않습니다.");
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 성공 - 일반회원 - 닉네임, 비밀번호 변경")
    void editSuccess_NicknameAndPassword() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("edit Tester", "12345");
        Member member = getMember();

        doReturn(Optional.empty()).when(memberService).findByNickname(any(String.class));
        doReturn(false).when(passwordEncoder).matches(any(String.class), any(String.class));
        doReturn("12345").when(passwordEncoder).encode(any(String.class));

        // WHEN
        RsData rsData = memberEditService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("회원 정보가 수정되었습니다.");
        verify(memberService, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 성공 - 일반회원 - 비밀번호변경")
    void editSuccess_Password() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("tester", "12345");
        Member member = getMember();

        doReturn(Optional.of(member)).when(memberService).findByNickname(any(String.class));
        doReturn(false).when(passwordEncoder).matches(any(String.class), any(String.class));
        doReturn("12345").when(passwordEncoder).encode(any(String.class));

        // WHEN
        RsData rsData = memberEditService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("회원 정보가 수정되었습니다.");
        verify(memberService, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 성공 - 일반회원 - 닉네임변경")
    void editSuccess_Nickname() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("edit Tester", "1234");
        Member member = getMember();

        doReturn(Optional.empty()).when(memberService).findByNickname(any(String.class));
        doReturn(true).when(passwordEncoder).matches(any(String.class), any(String.class));
        doReturn("1234").when(passwordEncoder).encode(any(String.class));

        // WHEN
        RsData rsData = memberEditService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("회원 정보가 수정되었습니다.");
        verify(memberService, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 성공 - 소셜회원 - 닉네임변경")
    void editSuccess_SocialNickname() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("edit Tester", "");
        Member member = Member.builder()
                .password("")
                .nickname("tester")
                .providerType(KAKAO)
                .build();

        doReturn(Optional.empty()).when(memberService).findByNickname(any(String.class));

        // WHEN
        RsData rsData = memberEditService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("S-1");
        assertThat(rsData.getMsg()).isEqualTo("회원 정보가 수정되었습니다.");
        verify(memberService, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(0)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 일반회원 - 수정된 값 없음")
    void editFail_NoChanges() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("tester", "1234");
        Member member = getMember();

        doReturn(Optional.of(member)).when(memberService).findByNickname(any(String.class));
        doReturn(true).when(passwordEncoder).matches(any(String.class), any(String.class));

        // WHEN
        RsData rsData = memberEditService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("수정된 정보가 없습니다!");
        verify(memberService, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 소셜회원 - 수정된 값 없음")
    void editSocialFail_NoChanges() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("tester", "");
        Member member = Member.builder()
                .password("")
                .nickname("tester")
                .providerType(KAKAO)
                .build();

        doReturn(Optional.of(member)).when(memberService).findByNickname(any(String.class));

        // WHEN
        RsData rsData = memberEditService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("수정된 정보가 없습니다!");
        verify(memberService, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(0)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 전체회원 - 중복된 닉네임")
    void editFail_DuplicateNickname() {
        // GIVEN
        EditRequestDto editRequestDto = getEditRequestDto("test", "1234");
        Member member = getMember();

        doReturn(Optional.of(member)).when(memberService).findByNickname(any(String.class));

        // WHEN
        RsData rsData = memberEditService.update(member, editRequestDto);

        // THEN
        assertThat(rsData.getResultCode()).isEqualTo("F-1");
        assertThat(rsData.getMsg()).isEqualTo("이미 존재하는 닉네임 입니다.");
        verify(memberService, times(1)).findByNickname(any(String.class));
        verify(passwordEncoder, times(0)).matches(any(String.class), any(String.class));
        verify(passwordEncoder, times(0)).encode(any(String.class));
    }
}