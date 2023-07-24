package com.goodjob.api.controller.payment;

import com.goodjob.member.constant.Membership;
import com.goodjob.member.dto.request.JoinRequestDto;
import com.goodjob.member.entity.Member;
import com.goodjob.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void init() {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("test");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setNickname("tester");
        joinRequestDto.setEmail("test@test.com");

        memberService.join(joinRequestDto);
    }

    @Test
    @DisplayName("토스페이먼츠 결제 성공")
    void paymentSuccess() throws Exception {
        // GIVEN
        MvcResult loginResult = mockMvc
                .perform(post("/member/login")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("username", "test")
                        .param("password", "1234")
                )
                .andReturn();

        MockHttpServletResponse response = loginResult.getResponse();
        Cookie[] cookies = response.getCookies();

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(get("/payment/success")
                        .cookie(cookies)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("orderId", "1")
                        .param("paymentKey", "1234")
                        .param("amount", "5900")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PaymentController.class))
                .andExpect(handler().methodName("processPaymentSuccess"))
                .andExpect(redirectedUrlPattern("/**"));
    }

    @Test
    @DisplayName("토스페이먼츠 결제 실패 - 상품가격 조작")
    void paymentFail_InvalidAmount() throws Exception {
        // GIVEN
        MvcResult loginResult = mockMvc
                .perform(post("/member/login")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("username", "test")
                        .param("password", "1234")
                )
                .andReturn();

        MockHttpServletResponse response = loginResult.getResponse();
        Cookie[] cookies = response.getCookies();

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(get("/payment/success")
                        .cookie(cookies)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("orderId", "1")
                        .param("paymentKey", "1234")
                        .param("amount", "5000")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(PaymentController.class))
                .andExpect(handler().methodName("processPaymentSuccess"));
    }

    @Test
    @DisplayName("토스페이먼츠 결제 실패 - 중복구매")
    void paymentFail_AlreadyPurchased() throws Exception {
        // GIVEN
        // GIVEN
        MvcResult loginResult = mockMvc
                .perform(post("/member/login")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("username", "test")
                        .param("password", "1234")
                )
                .andReturn();

        MockHttpServletResponse response = loginResult.getResponse();
        Cookie[] cookies = response.getCookies();

        Member member = memberService.findByUsername("test").orElse(null);
        member.upgradeMembership(Membership.PREMIUM);

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(get("/payment/success")
                        .cookie(cookies)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("orderId", "1")
                        .param("paymentKey", "1234")
                        .param("amount", "5900")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(PaymentController.class))
                .andExpect(handler().methodName("processPaymentSuccess"));
    }

}