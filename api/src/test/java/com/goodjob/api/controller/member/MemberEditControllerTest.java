package com.goodjob.api.controller.member;

import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Profile("local")
class MemberEditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void genMember(String nickname) {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("tester1");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setNickname(nickname);

        memberService.join(joinRequestDto);
    }

    @Test
    @DisplayName("회원정보수정 성공")
    @WithUserDetails("test")
    void editSuccess() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/edit")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("nickname", "tester2")
                        .param("password", "12345")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberEditController.class))
                .andExpect(handler().methodName("edit"))
                .andExpect(redirectedUrlPattern("/member/me**"))
                .andExpect(model().hasNoErrors());

        Member member = memberService.findByUsername("test").orElse(null);
        assertThat(member.getNickname()).isEqualTo("tester2");
        assertThat(passwordEncoder.matches("12345", member.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원정보수정 실패 - bindingError")
    @WithUserDetails("test")
    void editFail_BindingError() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/edit")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("nickname", "edit tester2")
                        .param("password", "123")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberEditController.class))
                .andExpect(handler().methodName("edit"))
                .andExpect(view().name("member/edit"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("회원정보수정 실패 - 닉네임 중복")
    @WithUserDetails("test")
    void editFail_InvalidInput() throws Exception {
        // GIVEN
        genMember("tester1");

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/edit")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("nickname", "tester1")
                        .param("password", "1234")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MemberEditController.class))
                .andExpect(handler().methodName("edit"))
                .andExpect(view().name("common/js"));
    }

    @Test
    @DisplayName("회원정보수정 실패 - 수정값 없음")
    @WithUserDetails("test")
    void editFail_NoChanges() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/edit")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("nickname", "tester")
                        .param("password", "1234")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MemberEditController.class))
                .andExpect(handler().methodName("edit"))
                .andExpect(view().name("common/js"));
    }

    @Test
    @DisplayName("회원정보수정 요청 시 현재 비밀번호 확인 성공")
    @WithUserDetails("test")
    void editSuccess_CurrentPasswordMatch() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/edit/confirm/password")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("passwordToEdit", "1234")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberEditController.class))
                .andExpect(handler().methodName("confirmPassword"))
                .andExpect(redirectedUrlPattern("/member/edit**"));
    }

    @Test
    @DisplayName("회원정보수정 요청 시 현재 비밀번호 확인 실패")
    @WithUserDetails("test")
    void editFail_CurrentPasswordMatch() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/edit/confirm/password")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("passwordToEdit", "12345")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberEditController.class))
                .andExpect(handler().methodName("confirmPassword"))
                .andExpect(redirectedUrlPattern("/member/me**"));
    }
}