package com.goodjob.core.domain.payment.service;

import com.goodjob.core.domain.payment.dto.request.PaymentRequestDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class PaymentService {


    public HttpURLConnection sendPaymentRequest(String clientSecret, String tossUrl, PaymentRequestDto paymentRequestDto) throws IOException {

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(clientSecret.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        URL url = new URL(tossUrl + paymentRequestDto.getPaymentKey());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        JSONObject obj = new JSONObject();
        obj.put("orderId", paymentRequestDto.getOrderId());
        obj.put("amount", paymentRequestDto.getAmount());

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        return connection;
    }

    public JSONObject getPaymentResponse(HttpURLConnection connection, boolean isSuccess) throws IOException, ParseException, ParseException {
        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        return jsonObject;
    }
}
