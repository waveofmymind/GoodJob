package com.goodjob.core.domain.mentoring.service;

import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.repository.MemberRepository;
import com.goodjob.core.domain.mentoring.dto.request.MentoringRequestDto;
import com.goodjob.core.domain.mentoring.entity.Mentoring;
import com.goodjob.core.domain.mentoring.repository.MentoringRepository;
import com.goodjob.core.global.base.jwt.JwtProvider;
import com.goodjob.core.global.base.rsData.RsData;
import org.apache.zookeeper.Op;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Collections;
import java.util.Optional;

import static com.goodjob.core.domain.member.constant.Membership.FREE;
import static com.goodjob.core.domain.member.constant.ProviderType.GOODJOB;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentoringServiceTest {
    @Mock
    private MentoringRepository mentoringRepository;

    @InjectMocks
    private MentoringService mentoringService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(mentoringService);
    }

    private Member singUpMember() {
        return Member.builder()
                .id(1L)
                .username("test")
                .password("1234")
                .nickname("tester")
                .email("test@naver.com")
                .membership(FREE)
                .providerType(GOODJOB)
                .build();
    }

    private Mentoring createMentoring() {
        Member member = singUpMember();
        return Mentoring.builder()
                .member(member)
                .id(1L)
                .title("title test")
                .content("content test")
                .job("백엔드")
                .career("신입(1년 이하)")
                .currentJob("네이버")
                .preferredTime("자유")
                .build();
    }

    private MentoringRequestDto createMentoringRequestDto() {
        MentoringRequestDto mentoringRequestDto = new MentoringRequestDto();
        mentoringRequestDto.setTitle("title test");
        mentoringRequestDto.setContent("content test");
        mentoringRequestDto.setJob("백엔드");
        mentoringRequestDto.setCareer("신입(1년 이하)");
        mentoringRequestDto.setCurrentJob("네이버");
        mentoringRequestDto.setPreferredTime("자유");

        return mentoringRequestDto;
    }

    @Test
    @DisplayName("멘토링 생성")
    void t001() {
        // GIVEN
        MentoringRequestDto mentoringRequestDto = createMentoringRequestDto();
        Member member = singUpMember();

        // WHEN
        RsData<Mentoring> mentoringRsData = mentoringService.createMentoring(member, mentoringRequestDto);

        // THEN
        assertThat(mentoringRsData.getResultCode()).isEqualTo("S-1");
        assertThat(mentoringRsData.getData().getTitle()).isEqualTo("title test");
        assertThat(mentoringRsData.getData().getMember().getUsername()).isEqualTo("test");
    }

    @Test
    @DisplayName("멘토링 수정")
    void t002() {
        // GIVEN
        MentoringRequestDto mentoringRequestDto = createMentoringRequestDto();
        Mentoring mentoring = createMentoring();
        Member member = singUpMember();

        doReturn(Optional.of(mentoring)).when(mentoringRepository).findById(anyLong());

        // WHEN
        mentoringRequestDto.setTitle("title change");
        mentoringRequestDto.setContent("content change");
        mentoringRequestDto.setJob("백엔드 change");
        mentoringRequestDto.setCareer("신입(1년 이하) change");
        mentoringRequestDto.setCurrentJob("네이버 change");
        mentoringRequestDto.setPreferredTime("자유 change");
        RsData<Mentoring> mentoringRsData = mentoringService.updateMentoring(member, 1L, mentoringRequestDto);

        // THEN
        assertThat(mentoringRsData.getResultCode()).isEqualTo("S-1");
        assertThat(mentoringRsData.getData().getTitle()).isEqualTo("title change");
        assertThat(mentoringRsData.getData().getContent()).isEqualTo("content change");
        assertThat(mentoringRsData.getData().getJob()).isEqualTo("백엔드 change");
        assertThat(mentoringRsData.getData().getCareer()).isEqualTo("신입(1년 이하) change");
        assertThat(mentoringRsData.getData().getCurrentJob()).isEqualTo("네이버 change");
        assertThat(mentoringRsData.getData().getPreferredTime()).isEqualTo("자유 change");
        assertThat(mentoringRsData.getData().getMember().getUsername()).isEqualTo("test");
    }

    @Test
    @DisplayName("멘토링 수정 실패 (권한 없음)")
    void t003() {
        // GIVEN
        MentoringRequestDto mentoringRequestDto = createMentoringRequestDto();
        Mentoring mentoring = createMentoring();
        Member member = Member.builder()    // 멘토링을 작성한 사람과 다른 멤버
                .id(2L)
                .username("test2")
                .password("1234")
                .nickname("tester2")
                .email("test2@naver.com")
                .membership(FREE)
                .providerType(GOODJOB)
                .build();

        doReturn(Optional.of(mentoring)).when(mentoringRepository).findById(anyLong());

        // WHEN
        mentoringRequestDto.setTitle("title change");
        RsData<Mentoring> mentoringRsData = mentoringService.updateMentoring(member, 1L, mentoringRequestDto);

        // THEN
        assertThat(mentoringRsData.getResultCode()).isEqualTo("F-3");
    }

    @Test
    @DisplayName("멘토링 삭제")
    void t004() {
        // GIVEN
        Mentoring mentoring = createMentoring();
        Member member = singUpMember();

        doReturn(Optional.of(mentoring)).when(mentoringRepository).findById(anyLong());

        // WHEN
        RsData<Mentoring> mentoringRsData = mentoringService.deleteMentoring(member, 1L);

        // THEN
        assertThat(mentoringRsData.getResultCode()).isEqualTo("S-1");
        assertThat(mentoringRsData.getData().isDeleted()).isEqualTo(true);
    }

    @Test
    @DisplayName("멘토링 삭제 실패(권한 없음)")
    void t005() {
        // GIVEN
        Mentoring mentoring = createMentoring();
        Member member = Member.builder()    // 멘토링을 작성한 사람과 다른 멤버
                .id(2L)
                .username("test2")
                .password("1234")
                .nickname("tester2")
                .email("test2@naver.com")
                .membership(FREE)
                .providerType(GOODJOB)
                .build();

        doReturn(Optional.of(mentoring)).when(mentoringRepository).findById(anyLong());

        // WHEN
        RsData<Mentoring> mentoringRsData = mentoringService.deleteMentoring(member, 1L);

        // THEN
        assertThat(mentoringRsData.getResultCode()).isEqualTo("F-3");
    }

    @Test
    @DisplayName("멘토링 가져오기")
    void t006() {
        // GIVEN
        Mentoring mentoring = createMentoring();

        doReturn(Optional.of(mentoring)).when(mentoringRepository).findById(anyLong());

        // WHEN
        RsData<Mentoring> mentoringRsData = mentoringService.getMentoring(1L);

        // THEN
        assertThat(mentoringRsData.getResultCode()).isEqualTo("S-1");
        assertThat(mentoringRsData.getData().getTitle()).isEqualTo("title test");
        assertThat(mentoringRsData.getData().getMember().getUsername()).isEqualTo("test");
    }
}