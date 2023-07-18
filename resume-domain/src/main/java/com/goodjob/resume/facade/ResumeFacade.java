package com.goodjob.resume.facade;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.resume.adaptor.outs.persistence.KafkaPredictionProducer;
import com.goodjob.common.error.ErrorCode;
import com.goodjob.common.error.exception.BusinessException;
import com.goodjob.resume.dto.request.CreatePromptRequest;
import com.goodjob.resume.dto.request.ResumeRequest;
import com.goodjob.resume.dto.response.WhatGeneratedImproveResponse;
import com.goodjob.resume.dto.response.WhatGeneratedQuestionResponse;
import com.goodjob.resume.gpt.GptService;
import com.goodjob.resume.application.in.SavePredictionUseCase;
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
    private final KafkaPredictionProducer kafkaPredictionProducer;

    @KafkaListener(topics = "question-prod", groupId = "gptgroup")
    public void generatedQuestionResponseWithKafka(String message) throws JsonProcessingException {
        try {
            CreatePromptRequest request = objectMapper.readValue(message, CreatePromptRequest.class);

            // memberId가 null이 아닌 경우에만 실행
            if (request.getMemberId() != null) {
                List<ResumeRequest> resumeData = request.getResumeRequests();
                WhatGeneratedQuestionResponse response = gptService.createdExpectedQuestionsAndAnswer(request.getJob(), request.getCareer(), resumeData);
                savePredictionUseCase.savePrediction(response.toServiceDto(request.getMemberId()));

            } else {
                log.info("비 로그인 유저이므로 데이터가 남지 않습니다.");
            }

        } catch (Exception e) {
            CreatePromptRequest request = objectMapper.readValue(message, CreatePromptRequest.class);
            kafkaPredictionProducer.sendError(request.getMemberId().toString());
            throw new BusinessException(ErrorCode.CREATE_PREDICTION_QUESTION);
        }

    }

    @KafkaListener(topics = "advice-prod", groupId = "gptgroup")
    public void generateAdviceWithKafka(String message) throws JsonProcessingException {
        try {
            CreatePromptRequest request = objectMapper.readValue(message, CreatePromptRequest.class);

            // memberId가 null이 아닌 경우에만 실행
            if (request.getMemberId() != null) {
                List<ResumeRequest> resumeData = request.getResumeRequests();
                WhatGeneratedImproveResponse response = gptService.createdImprovementPointsAndAdvice(request.getJob(), request.getCareer(), resumeData);
                savePredictionUseCase.savePrediction(response.toServiceDto(request.getMemberId()));
            } else {
                log.info("비 로그인 유저이므로 데이터가 남지 않습니다.");
            }

        } catch (Exception e) {
            CreatePromptRequest request = objectMapper.readValue(message, CreatePromptRequest.class);
            kafkaPredictionProducer.sendError(request.getMemberId().toString());
            throw new BusinessException(ErrorCode.CREATE_PREDICTION_QUESTION);
        }
    }
}
