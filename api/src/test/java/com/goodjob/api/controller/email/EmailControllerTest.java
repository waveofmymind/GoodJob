package com.goodjob.api.controller.email;


import com.goodjob.common.email.entity.SendEmailLog;
import com.goodjob.common.email.service.EmailService;
import com.goodjob.common.redis.RedisUt;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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
@Profile("local")
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisUt redisUt;

    @Test
    @DisplayName("회원가입코드 이메일 전송 성공")
    void sendEmailSuccess() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/email/send")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test@test.com")
                )
                .andDo(print());

        // THEN
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(EmailController.class))
                .andExpect(handler().methodName("sendEmail"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Cookie email = response.getCookie("email");

        SendEmailLog sendEmailLog = emailService.findByEmail("test@test.com").get();

        assertThat(email.getValue()).isEqualTo("test@test.com");
        assertThat(sendEmailLog.getResultCode()).startsWith("S-");
    }

    @Test
    @DisplayName("회원가입코드 검증 성공")
    void verifySuccess() throws Exception {
        // GIVEN
        // 이메일 전송 - verificationCode, 쿠키 발급
        MvcResult sendResult = mockMvc
                .perform(post("/email/send")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test@test.com")
                )
                .andReturn();

        MockHttpServletResponse response = sendResult.getResponse();
        Cookie email = response.getCookie("email");
        String verificationCode = redisUt.getValue("test@test.com");

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/email/verify")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .cookie(email)
                        .param("verificationCode", verificationCode)
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(EmailController.class))
                .andExpect(handler().methodName("verifyEmail"))
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").value("인증 코드가 확인되었습니다."))
                .andReturn();
    }

    @Test
    @DisplayName("회원가입코드 검증 실패 - 잘못된 인증코드")
    void verifyFail_InvalidVerificationCode() throws Exception {
        // GIVEN
        // 이메일 전송 - verificationCode, 쿠키 발급
        MvcResult sendResult = mockMvc
                .perform(post("/email/send")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test@test.com")
                )
                .andReturn();

        MockHttpServletResponse response = sendResult.getResponse();
        Cookie email = response.getCookie("email");

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/email/verify")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .cookie(email)
                        .param("verificationCode", "1234")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(EmailController.class))
                .andExpect(handler().methodName("verifyEmail"))
                .andExpect(jsonPath("$.resultCode").value("F-1"))
                .andExpect(jsonPath("$.msg").value("잘못된 인증 코드입니다."))
                .andReturn();
    }

    @Test
    @DisplayName("회원가입코드 검증 실패 - 쿠키에 이메일 정보 없음")
    void verifyFail_NoEmailInCookie() throws Exception {
        // GIVEN
        // 이메일 전송 - verificationCode 발급
        mockMvc
                .perform(post("/email/send")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test@test.com")
                );

        String verificationCode = redisUt.getValue("test@test.com");

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/email/verify")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("verificationCode", verificationCode)
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(EmailController.class))
                .andExpect(handler().methodName("verifyEmail"))
                .andExpect(jsonPath("$.resultCode").value("F-1"))
                .andExpect(jsonPath("$.msg").value("이메일 정보가 없습니다. 새로고침 후 다시 시도해주세요."));
    }

    @Test
    @DisplayName("회원가입코드 검증 실패 - 이메일 정보 조작된 쿠키")
    void verifyFail_InvalidEmailInCookie() throws Exception {
        // GIVEN
        // 이메일 전송 - verificationCode, 쿠키 발급
        MvcResult sendResult = mockMvc
                .perform(post("/email/send")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test@test.com")
                )
                .andReturn();

        MockHttpServletResponse response = sendResult.getResponse();
        Cookie email = response.getCookie("email");
        email.setValue("1234");
        String verificationCode = redisUt.getValue("test@test.com");

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/email/verify")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .cookie(email)
                        .param("verificationCode", verificationCode)
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(EmailController.class))
                .andExpect(handler().methodName("verifyEmail"))
                .andExpect(jsonPath("$.resultCode").value("F-1"))
                .andExpect(jsonPath("$.msg").value("이메일 정보가 잘못되었습니다. 올바른 이메일을 입력하여 다시 시도해주세요."));
    }
}