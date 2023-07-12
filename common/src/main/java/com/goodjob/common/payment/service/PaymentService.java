package com.goodjob.common.payment.service;

import com.goodjob.common.payment.dto.request.PaymentRequestDto;
import com.goodjob.common.payment.entitiy.Payment;
import com.goodjob.common.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public HttpURLConnection sendPaymentRequest(String clientSecret, String tossUrl, PaymentRequestDto paymentRequestDto) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(clientSecret.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        HttpURLConnection connection = getConnection(tossUrl, paymentRequestDto, authorizations);

        JSONObject obj = new JSONObject();
        obj.put("orderId", paymentRequestDto.getOrderId());
        obj.put("amount", paymentRequestDto.getAmount());

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        return connection;
    }

    public JSONObject getPaymentResponse(HttpURLConnection connection, boolean isSuccess) throws IOException, ParseException {
        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        return jsonObject;
    }

    @Transactional
    public void save(JSONObject jsonObject) {
        Payment payment = Payment.builder()
                .orderId((String) jsonObject.get("orderId"))
                .productName((String) jsonObject.get("orderName"))
                .paymentAmount((Long) jsonObject.get("balanceAmount"))
                .paymentMethod((String) jsonObject.get("method"))
                .paymentStatus((String) jsonObject.get("status"))
                .build();

        paymentRepository.save(payment);
    }

    public Optional<Payment> findByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    private HttpURLConnection getConnection(String tossUrl, PaymentRequestDto paymentRequestDto, String authorizations) throws IOException {
        URL url = new URL(tossUrl + paymentRequestDto.getPaymentKey());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        return connection;
    }
}
