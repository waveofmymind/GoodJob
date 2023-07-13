package com.goodjob.api.controller.mentoring;

import com.goodjob.common.rsData.RsData;
import com.goodjob.member.entity.Member;
import com.goodjob.mentoring.domain.mentoring.dto.request.MentoringRequestDto;
import com.goodjob.mentoring.domain.mentoring.entity.Mentoring;
import com.goodjob.mentoring.domain.mentoring.service.MentoringService;
import com.goodjob.core.global.rq.Rq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.goodjob.member.constant.Membership.FREE;
import static com.goodjob.member.constant.ProviderType.GOODJOB;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MentoringControllerTest {
    @InjectMocks
    private MentoringController mentoringController;

    @Mock
    private MentoringService mentoringService;

    @Mock
    private Rq rq;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(mentoringController).build();
    }

    private Member singUpMember() {
        return Member.builder()
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
                .title("title test")
                .content("content test")
                .job("백엔드")
                .career("신입(1년 이하)")
                .currentJob("네이버")
                .preferredTime("자유")
                .build();
    }

    @Test
    @DisplayName("멘토링 등록")
    void t001() throws Exception {
        // Given
        Mentoring mentoring = createMentoring();

        RsData<Mentoring> mentoringRsData = new RsData<>("S-1", "멘토링을 성공적으로 생성하였습니다.", mentoring);

        doReturn(mentoringRsData).when(mentoringService).createMentoring(isNull(), any(MentoringRequestDto.class));

        // When
        ResultActions resultActions = mockMvc
                .perform(post("/mentoring/create")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("title", "title test")
                        .param("content", "test content")
                        .param("job", "백엔드")
                        .param("career", "신입(1년 이하)")
                        .param("currentJob", "네이버")
                        .param("preferredTime", "자유")
                )
                .andDo(MockMvcResultHandlers.print());

        // Then
        resultActions
                .andExpect(handler().handlerType(MentoringController.class))
                .andExpect(handler().methodName("createMentoring"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/mentoring/detail/{id}"));
    }

    @Test
    @DisplayName("멘토링 수정")
    void t002() throws Exception {
        // Given
        Mentoring mentoring = createMentoring();

        RsData<Mentoring> mentoringRsData = new RsData<>("S-1", "멘토링이 수정되었습니다.", mentoring);

        doReturn(mentoringRsData).when(mentoringService).updateMentoring(isNull(), anyLong(), any(MentoringRequestDto.class));
        doReturn("redirect:/mentoring/detail/1").when(rq).redirectWithMsg(any(String.class), any(RsData.class));

        // When
        ResultActions resultActions = mockMvc
                .perform(post("/mentoring/update/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("title", "title test")
                        .param("content", "test content")
                        .param("job", "백엔드")
                        .param("career", "신입(1년 이하)")
                        .param("currentJob", "네이버")
                        .param("preferredTime", "자유")
                )
                .andDo(MockMvcResultHandlers.print());

        // Then
        resultActions
                .andExpect(handler().handlerType(MentoringController.class))
                .andExpect(handler().methodName("updateMentoring"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/mentoring/detail/{id}"));
    }

    @Test
    @DisplayName("멘토링 삭제")
    void t003() throws Exception {
        // Given
        Mentoring mentoring = createMentoring();
        RsData<Mentoring> mentoringRsData = new RsData<>("S-1", "멘토링이 삭제되었습니다.", mentoring);

        doReturn(mentoringRsData).when(mentoringService).deleteMentoring(isNull(), anyLong());
        doReturn("redirect:/mentoring/list").when(rq).redirectWithMsg(any(String.class), any(RsData.class));

        // When
        ResultActions resultActions = mockMvc
                .perform(get("/mentoring/delete/1")
                )
                .andDo(MockMvcResultHandlers.print());

        // Then
        resultActions
                .andExpect(handler().handlerType(MentoringController.class))
                .andExpect(handler().methodName("deleteMentoring"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/mentoring/list/**"));
    }

    @Test
    @DisplayName("멘토링 조회")
    void t004() throws Exception {
        // Given
        Mentoring mentoring = createMentoring();
        RsData<Mentoring> mentoringRsData = new RsData<>("S-1", "멘토링에 대한 정보를 가져옵니다.", mentoring);

        doReturn(mentoringRsData).when(mentoringService).getMentoring(anyLong());

        // When
        ResultActions resultActions = mockMvc
                .perform(get("/mentoring/detail/1")
                )
                .andDo(MockMvcResultHandlers.print());

        // Then
        resultActions
                .andExpect(handler().handlerType(MentoringController.class))
                .andExpect(handler().methodName("detailMentoring"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
