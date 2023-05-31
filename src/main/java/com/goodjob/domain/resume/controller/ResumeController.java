package com.goodjob.domain.resume.controller;

import com.goodjob.domain.resume.dto.request.CreatePromptRequest;
import com.goodjob.domain.resume.dto.response.WhatGeneratedImproveResponse;
import com.goodjob.domain.resume.dto.response.WhatGeneratedQuestionResponse;
import com.goodjob.domain.resume.facade.ResumeFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeFacade resumeFacade;


    @GetMapping("/questions")
    public String showQuestionForm() {
        return "resume/question-request";
    }

    @PostMapping("/questions")
    public String generateQuestion(@ModelAttribute CreatePromptRequest request, Model model) {

        WhatGeneratedQuestionResponse generated = resumeFacade.generateQuestion(request);
        log.info(generated.toString());
        model.addAttribute("predictionResponses", generated.predictionResponse());

        return "resume/question-result";
    }

    @GetMapping("/advices")
    public String showAdviceForm() {
        return "resume/advice-request";
    }

    @PostMapping("/advices")
    public String generateAdvice(@ModelAttribute CreatePromptRequest request, Model model) {

        WhatGeneratedImproveResponse generated = resumeFacade.generateAdvice(request);
        log.info(generated.toString());
        model.addAttribute("improveResponses", generated.improvementResponse());

        return "resume/advice-result";
    }


}
