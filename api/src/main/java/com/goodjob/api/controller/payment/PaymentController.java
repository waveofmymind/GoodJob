package com.goodjob.api.controller.payment;

import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.common.payment.dto.request.PaymentRequestDto;
import com.goodjob.common.payment.service.PaymentService;
import com.goodjob.core.global.rq.Rq;
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

    @Value("${payment.amount}")
    private long amount;

    private final PaymentService paymentService;

    private final MemberService memberService;

    private final Rq rq;

    @GetMapping("/showPage")
    public String showPaymentPage(Model model) {

        model.addAttribute("amount", amount);
        return "payment/payment-page";
    }

    @GetMapping("/success")
    public String processPaymentSuccess(PaymentRequestDto paymentRequestDto) throws Exception {
        if (amount != paymentRequestDto.getAmount()) { // 상품가격 다른 경우
            return rq.historyBack("잘못된 접근입니다.");
        }

        if (rq.getMember().isPremium()) { // 이미 구매한 경우
            return rq.historyBack("이미 프리미엄 회원입니다.");
        }

        HttpURLConnection connection = paymentService.sendPaymentRequest(clientSecret, tossUrl, paymentRequestDto);

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;

        JSONObject jsonObject = paymentService.getPaymentResponse(connection, isSuccess);
        paymentService.save(jsonObject);
        memberService.upgradeToPremiumMembership(rq.getMember());

        return rq.redirectWithMsg("/", "결제 완료되었습니다.");
    }

    @GetMapping("/fail")
    public String processPaymentFail(
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code
    ) throws Exception {

        return rq.redirectWithMsg("/", "결제 실패: %s".formatted(message));
    }
}