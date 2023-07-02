package com.goodjob.api.controller.member;

import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.global.base.rsData.RsData;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Mock
    private Rq rq;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    @DisplayName("일반 회원가입 성공")
    void joinSuccess() throws Exception {
        // Given
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("test");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setConfirmPassword("1234");
        joinRequestDto.setNickname("tester");
        joinRequestDto.setEmail("test@naver.com");

        Member member = Member.builder()
                .username("test")
                .password("1234")
                .nickname("tester")
                .email("test@naver.com")
                .userRole("free")
                .providerType("GOODJOB")
                .build();

        RsData<Member> joinRsData = new RsData<>("S-1", "%s님의 회원가입이 완료되었습니다.".formatted(joinRequestDto.getNickname()), member);

        doReturn(joinRsData).when(memberService).join(any(JoinRequestDto.class));
        doReturn("redirect:/member/login").when(rq).redirectWithMsg(any(String.class), any(RsData.class));

        // When
        ResultActions resultActions = mockMvc
                .perform(post("/member/join")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("username", "test")
                        .param("password", "1234")
                        .param("confirmPassword", "1234")
                        .param("nickname", "tester")
                        .param("email", "test@naver.com")
                )
                .andDo(MockMvcResultHandlers.print());

        // Then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(redirectedUrlPattern("/member/login/**"));

        verify(memberService, times(1)).join(any(JoinRequestDto.class));
        verify(rq, times(1)).redirectWithMsg(any(String.class), any(RsData.class));
    }
}