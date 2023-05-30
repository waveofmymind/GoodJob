package com.goodjob.domain.resume.facade;

import com.goodjob.domain.gpt.GptService;
import com.goodjob.domain.resume.dto.request.CreatePromptRequest;
import com.goodjob.domain.resume.dto.request.ResumeRequest;
import com.goodjob.domain.resume.dto.response.WhatGeneratedResponse;
import com.goodjob.domain.resume.mapper.GenerateExpectedMapper;
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


    public WhatGeneratedResponse generate(final CreatePromptRequest request) {

        log.debug("이력서 생성 update request : {}", request.toString());


        ResumeRequest resumeData = generateExpectedMapper.toResumeData(request.resumeType(), request.content());

        WhatGeneratedResponse response = gptService.createdExpectedQuestionsAndAnswer(request.job(), request.career(), List.of(resumeData));

        log.info(response.toString());
        return response;
    }
}
