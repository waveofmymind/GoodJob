package com.goodjob.api.controller.payment;

import com.goodjob.core.domain.member.constant.Membership;
import com.goodjob.core.domain.member.entity.Member;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Profile("local")
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("토스페이먼츠 결제 성공")
    @WithUserDetails("test")
    void paymentSuccess() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(get("/payment/success")
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
    @WithUserDetails("test")
    void paymentFail_InvalidAmount() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(get("/payment/success")
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
    @WithUserDetails("test")
    void paymentFail_AlreadyPurchased() throws Exception {
        // GIVEN
        Member member = memberService.findByUsername("test").orElse(null);
        member.upgradeMembership(Membership.PREMIUM);

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(get("/payment/success")
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