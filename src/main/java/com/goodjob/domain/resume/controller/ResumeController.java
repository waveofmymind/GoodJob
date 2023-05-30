package com.goodjob.domain.resume.controller;

import com.goodjob.domain.gpt.GptService;
import com.goodjob.domain.resume.dto.request.CreatePromptRequest;
import com.goodjob.domain.resume.dto.response.WhatGeneratedResponse;
import com.goodjob.domain.resume.facade.ResumeFacade;
import com.goodjob.global.base.rq.Rq;
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


    @GetMapping("/generate")
    public String showGenerate() {
        return "/resume/request";
    }

    @PostMapping("/generate")
    public String generate(@ModelAttribute CreatePromptRequest request, Model model) {

        WhatGeneratedResponse generated = resumeFacade.generate(request);
        log.info(generated.toString());
        model.addAttribute("predictionResponses", generated.predictionResponse());

        return "/resume/result";
    }


}
