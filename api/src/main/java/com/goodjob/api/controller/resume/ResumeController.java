package com.goodjob.api.controller.resume;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.core.domain.resume.dto.request.CreatePromptRequest;
import com.goodjob.core.domain.resume.dto.request.ResumeRequest;
import com.goodjob.core.domain.resume.dto.response.WhatGeneratedImproveResponse;
import com.goodjob.core.domain.resume.facade.ResumeFacade;
import com.goodjob.core.global.rq.Rq;
import com.goodjob.core.domain.resume.adaptor.KafkaPredictionProducer;
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
    private final KafkaPredictionProducer kafkaPredictionProducer;
    private final ObjectMapper objectMapper;

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
        if (rq.getMember() == null) {
            request.setMemberId(null);
            //TODO: 비로그인시 별도 페이지 생성
        } else {
            request.setMemberId(rq.getMember().getId());
            kafkaPredictionProducer.sendQuestionRequest(objectMapper.writeValueAsString(request));
        }

        return "resume/request-complete";
    }

    @GetMapping("/advices")
    public String showAdviceForm() {
        return "resume/advice-request";
    }

    @PostMapping("/advices")
    public String generateAdvice(@ModelAttribute CreatePromptRequest request) throws JsonProcessingException {
        if (rq.getMember() == null) {
            request.setMemberId(null);
            //TODO: 비로그인시 별도 페이지 생성
        } else {
            request.setMemberId(rq.getMember().getId());
            kafkaPredictionProducer.sendAdviceRequest(objectMapper.writeValueAsString(request));
        }

        return "resume/request-complete";
    }


}
