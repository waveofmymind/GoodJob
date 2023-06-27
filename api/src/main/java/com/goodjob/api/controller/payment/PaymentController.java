package com.goodjob.api.controller.payment;

import com.goodjob.core.domain.payment.dto.request.PaymentRequestDto;
import com.goodjob.core.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.HttpURLConnection;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    @Value("${payment.client-secret}")
    private String clientSecret;

    @Value("${payment.url}")
    private String tossUrl;

    private final PaymentService paymentService;

    @GetMapping("/success")
    public String paymentResult(PaymentRequestDto paymentRequestDto, Model model) throws Exception {

        HttpURLConnection connection = paymentService.sendPaymentRequest(clientSecret, tossUrl, paymentRequestDto);

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;

        JSONObject jsonObject = paymentService.getPaymentResponse(connection, isSuccess);

        model.addAttribute("isSuccess", isSuccess);
        model.addAttribute("responseStr", jsonObject.toJSONString());
        model.addAttribute("method", (String) jsonObject.get("method"));
        model.addAttribute("orderName", (String) jsonObject.get("orderName"));

        if (((String) jsonObject.get("method")) != null) {
            if (((String) jsonObject.get("method")).equals("카드")) {
                model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
            } else if (((String) jsonObject.get("method")).equals("가상계좌")) {
                model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
            } else if (((String) jsonObject.get("method")).equals("계좌이체")) {
                model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
            } else if (((String) jsonObject.get("method")).equals("휴대폰")) {
                model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
            }
        } else {
            model.addAttribute("code", (String) jsonObject.get("code"));
            model.addAttribute("message", (String) jsonObject.get("message"));
        }

        return "payment/success";
    }

    @GetMapping("/fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code
    ) throws Exception {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "payment/fail";
    }

}