package com.goodjob.api.controller.member;


import com.goodjob.common.email.entity.SendEmailLog;
import com.goodjob.common.email.service.EmailService;
import com.goodjob.member.entity.Member;
import com.goodjob.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
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
class MemberRecoverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("아이디찾기 성공")
    void recoverUsernameSuccess() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/recover/username")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test@naver.com")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberRecoverController.class))
                .andExpect(handler().methodName("recoverUsername"))
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").value("가입하신 아이디는 te** 입니다."));
    }

    @Test
    @DisplayName("아이디찾기 실패 - 이메일 정보 없음")
    void recoverUsernameFail_NoEmail() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/recover/username")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "fail@test.com")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberRecoverController.class))
                .andExpect(handler().methodName("recoverUsername"))
                .andExpect(jsonPath("$.resultCode").value("F-1"))
                .andExpect(jsonPath("$.msg").value("해당 이메일을 가진 회원이 없습니다."));
    }

    @Test
    @DisplayName("비밀번호찾기 성공")
    void recoverPasswordSuccess() throws Exception {
        // GIVEN
        Member givenMember = memberService.findByUsername("test").orElse(null);

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/recover/password")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("username", "test")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberRecoverController.class))
                .andExpect(handler().methodName("recoverPassword"))
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").value("가입하신 메일주소로 임시비밀번호가 발송되었습니다."));

        Member member = memberService.findByUsername("test").orElse(null);
        // 비밀번호 변경 확인
        assertThat((member.getPassword())).isEqualTo(givenMember.getPassword());

        // 이메일 발송 확인
        SendEmailLog sendEmailLog = emailService.findByUsername(member.getUsername()).orElse(null);
        assertThat(sendEmailLog.getResultCode()).isEqualTo("S-1");
    }
}