package com.goodjob.core.domain.resume.facade;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.core.domain.gpt.GptService;
import com.goodjob.core.domain.resume.usecase.SavePredictionUseCase;
import com.goodjob.core.domain.resume.dto.request.CreatePromptRequest;
import com.goodjob.core.domain.resume.dto.request.ResumeRequest;
import com.goodjob.core.domain.resume.dto.response.WhatGeneratedImproveResponse;
import com.goodjob.core.domain.resume.dto.response.WhatGeneratedQuestionResponse;
import com.goodjob.core.global.error.ErrorCode;
import com.goodjob.core.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeFacade {

    private final GptService gptService;
    private final ObjectMapper objectMapper;
    private final SavePredictionUseCase savePredictionUseCase;
    @KafkaListener(topics = "question-request", groupId = "gptgroup")
    public void generatedQuestionResponseWithKafka(String message) {
        try {
            CreatePromptRequest request = objectMapper.readValue(message, CreatePromptRequest.class);
            log.info("이력서 생성 update request : {}", message);

            // memberId가 null이 아닌 경우에만 실행
            if (request.getMemberId() != null) {
                List<ResumeRequest> resumeData = request.getResumeRequests();
                WhatGeneratedQuestionResponse response = gptService.createdExpectedQuestionsAndAnswer(request.getJob(), request.getCareer(), resumeData);
                savePredictionUseCase.savePrediction(response.toServiceDto(request.getMemberId()));
            } else {
                log.debug("비 로그인 유저이므로 데이터가 남지 않습니다.");
            }

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.CREATE_PREDICTION_QUESTION);
        }
    }

    @KafkaListener(topics = "answer-request", groupId = "gptgroup")
    public void generateAdviceWithKafka(String message) {
        try {
            CreatePromptRequest request = objectMapper.readValue(message, CreatePromptRequest.class);
            log.info("이력서 생성 update request : {}", message);

            // memberId가 null이 아닌 경우에만 실행
            if (request.getMemberId() != null) {
                List<ResumeRequest> resumeData = request.getResumeRequests();
                WhatGeneratedImproveResponse response = gptService.createdImprovementPointsAndAdvice(request.getJob(), request.getCareer(), resumeData);
                savePredictionUseCase.savePrediction(response.toServiceDto(request.getMemberId()));
            } else {
                log.debug("비 로그인 유저이므로 데이터가 남지 않습니다.");
            }

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.CREATE_PREDICTION_QUESTION);
        }
    }


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
