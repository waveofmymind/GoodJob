package com.goodjob.api.controller.member;

import com.goodjob.core.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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

        assertThat(memberService.findByNickname("tester2")).isPresent();
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

        assertThat(memberService.findByUsername("edit tester2")).isEmpty();
    }
}