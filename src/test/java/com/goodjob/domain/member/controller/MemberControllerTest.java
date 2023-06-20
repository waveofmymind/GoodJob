package com.goodjob.domain.member.controller;

import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MemberControllerTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("test");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setNickname("test");
        joinRequestDto.setEmail("test@test.com");

        memberService.join(joinRequestDto);
    }

    @Test
    @DisplayName("회원가입")
    void t01() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .param("username", "tester1")
                        .param("password", "1234")
                        .param("confirmPassword", "1234")
                        .param("nickname", "tester1")
                        .param("email", "tester1@naver.com")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(redirectedUrlPattern("/member/login**"));

        assertThat(memberService.findByUsername("tester1").isPresent()).isTrue();
    }

    @Test
    @DisplayName("로그인")
    void t02() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/login")
                        .param("username", "test")
                        .param("password", "1234")
                )
                .andDo(print());

        // THEN
        MvcResult mvcResult = resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(redirectedUrlPattern("/**"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Cookie jwtCookie = response.getCookie("accessToken");

        assertThat(jwtCookie).isNotNull();
    }

    @Test
    @DisplayName("로그아웃")
    void t03() throws Exception {
        // GIVEN
        mvc
                .perform(post("/member/login")
                        .param("username", "test")
                        .param("password", "1234")
                );

        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/logout")
                )
                .andDo(print());

        // THEN
        MvcResult mvcResult = resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("logout"))
                .andExpect(redirectedUrlPattern("/**"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Cookie jwtCookie = response.getCookie("accessToken");

        assertThat(jwtCookie).isNull();
    }
}