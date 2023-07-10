package com.goodjob.core.domain.payment.service;

import com.goodjob.core.domain.payment.dto.request.PaymentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private URL url;

    @Test
    @DisplayName("payment 요청 전송 성공")
    void sendPaymentRequestSuccess() throws IOException {
        // GIVEN
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setOrderId("1");
        paymentRequestDto.setPaymentKey("paymentKey");
        paymentRequestDto.setAmount(5900);

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode("1234".getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        // WHEN
        HttpURLConnection resultConnection = paymentService.sendPaymentRequest("1234", "https://api.tosspayments.com/v1/payments/", paymentRequestDto);

        // THEN
        assertThat(resultConnection.getRequestProperty("Authorizations")).isEqualTo(authorizations);
        assertThat(resultConnection.getRequestProperty("Authorization")).isEqualTo(authorizations);
        assertThat(resultConnection.getRequestProperty("Content-Type")).isEqualTo("application/json");
        assertThat(resultConnection.getRequestMethod()).isEqualTo("POST");
        assertThat(resultConnection.getDoOutput()).isEqualTo(true);
    }
}