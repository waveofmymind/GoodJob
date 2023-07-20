package com.goodjob.api.controller.resume;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.member.coin.CoinUt;
import com.goodjob.resume.adaptor.outs.persistence.ProducerAdapter;
import com.goodjob.resume.domain.ServiceType;
import com.goodjob.resume.dto.request.CreatePromptRequest;
import com.goodjob.resume.dto.request.ResumeRequest;
import com.goodjob.resume.dto.response.ResponsePredictionDto;
import com.goodjob.resume.dto.response.WhatGeneratedImproveResponse;
import com.goodjob.resume.dto.response.WhatGeneratedQuestionResponse;
import com.goodjob.resume.facade.PredictionFacade;
import com.goodjob.core.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/resumes")
public class ResumeController {

    private final Rq rq;
    private final ProducerAdapter producerAdapter;
    private final ObjectMapper objectMapper;
    private final PredictionFacade predictionFacade;
    private final CoinUt coinUt;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ResumeRequest.class, new ResumeRequestEditor());
    }


    @GetMapping("/questions")
    public String showQuestionForm() {
        return "resume/question-request";
    }

    @PostMapping("/questions")
    public String generateQuestion(@ModelAttribute CreatePromptRequest request) throws JsonProcessingException {

        request.setMemberId(rq.getMember().getId());

        boolean isServiceAvailable = coinUt.isServiceAvailable(rq.getMember());
        if (!isServiceAvailable) {
            return "resume/coin-shortage";
        }

        producerAdapter.sendQuestionRequest(objectMapper.writeValueAsString(request));

        return "resume/request-complete";
    }

    @GetMapping("/advices")
    public String showAdviceForm() {
        return "resume/advice-request";
    }

    @PostMapping("/advices")
    public String generateAdvice(@ModelAttribute CreatePromptRequest request) throws JsonProcessingException {
        request.setMemberId(rq.getMember().getId());
        request.setMemberId(rq.getMember().getId());
        boolean isServiceAvailable = coinUt.isServiceAvailable(rq.getMember());

        if (!isServiceAvailable) {
            return "resume/coin-shortage";
        }

        producerAdapter.sendAdviceRequest(objectMapper.writeValueAsString(request));


        return "resume/request-complete";
    }

    @GetMapping("/detail/{predictionId}")
    public String showDetailForm(@PathVariable("predictionId") Long id, Model model) {
        ResponsePredictionDto predictionDto = predictionFacade.getPredictionById(id);

        if (predictionDto.getServiceType() == ServiceType.EXPECTED_ADVICE) {
            model.addAttribute("improveResponses", WhatGeneratedImproveResponse.of(predictionDto.getTitles(), predictionDto.getContents()));
            return "resume/advice-result";
        } else {
            model.addAttribute("predictionResponses", WhatGeneratedQuestionResponse.of(predictionDto.getTitles(), predictionDto.getContents()));
            return "resume/question-result";
        }
    }
}
