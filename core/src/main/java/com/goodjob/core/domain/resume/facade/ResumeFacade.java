package com.goodjob.core.domain.resume.facade;


import com.goodjob.core.domain.gpt.GptService;
import com.goodjob.core.domain.resume.dto.request.CreatePromptRequest;
import com.goodjob.core.domain.resume.dto.request.ResumeRequest;
import com.goodjob.core.domain.resume.dto.response.WhatGeneratedImproveResponse;
import com.goodjob.core.domain.resume.dto.response.WhatGeneratedQuestionResponse;
import com.goodjob.core.domain.resume.mapper.GenerateExpectedMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeFacade {

    private final GptService gptService;
    private final GenerateExpectedMapper generateExpectedMapper;


    public WhatGeneratedQuestionResponse generateQuestion(final CreatePromptRequest request) {

        log.debug("이력서 생성 update request : {}", request.toString());
        List<ResumeRequest> resumeData = request.getResumeRequests();
        return gptService.createdExpectedQuestionsAndAnswer(request.getJob(), request.getCareer(), resumeData);
    }

    public WhatGeneratedImproveResponse generateAdvice(final CreatePromptRequest request) {

        log.debug("이력서 생성 update request : {}", request.toString());

        List<ResumeRequest> resumeData = request.getResumeRequests();

        return gptService.createdImprovementPointsAndAdvice(request.getJob(), request.getCareer(), resumeData);
    }
}
