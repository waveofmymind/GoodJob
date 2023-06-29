package com.goodjob.core.domain.resume.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.core.CoreConfigurationLoader;
import com.goodjob.core.domain.gpt.GptService;
import com.goodjob.core.domain.resume.dto.request.CreatePromptRequest;
import com.goodjob.core.domain.resume.dto.response.ImprovementResponse;
import com.goodjob.core.domain.resume.dto.response.PredictionResponse;
import com.goodjob.core.domain.resume.dto.response.WhatGeneratedImproveResponse;
import com.goodjob.core.domain.resume.dto.response.WhatGeneratedQuestionResponse;
import com.goodjob.core.domain.resume.usecase.SavePredictionUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeFacadeTest {

    @Mock
    private GptService gptService;

    @Mock
    private SavePredictionUseCase savePredictionUseCase;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ResumeFacade resumeFacade;

    @DisplayName("CreatePromptRequest가 주어지면, 면접 예상 질문이 생성된다.")
    @Test
    void testGeneratedQuestionResponseWithKafka() throws JsonProcessingException {
        // Given
        String message = "테스트 메시지";
        CreatePromptRequest promptRequest = new CreatePromptRequest(1L, "Software backend Developer", "Newcomer", null);
        WhatGeneratedQuestionResponse mockResponse = new WhatGeneratedQuestionResponse();
        mockResponse.predictionResponse().add(new PredictionResponse("question", "answer"));

        when(objectMapper.readValue(message, CreatePromptRequest.class)).thenReturn(promptRequest);

        when(gptService.createdExpectedQuestionsAndAnswer(any(), any(), any())).thenReturn(mockResponse);

        // When
        resumeFacade.generatedQuestionResponseWithKafka(message);

        // Then
        verify(savePredictionUseCase, times(1)).savePrediction(any());
    }


    @DisplayName("CreatePromptRequest가 주어지면, 이력서 검토 개선안이 생성된다.")
    @Test
    void testGeneratedAdviceResponseWithKafka() throws JsonProcessingException {
        // Given
        String message = "테스트 메시지";
        CreatePromptRequest promptRequest = new CreatePromptRequest(1L, "Software backend Developer", "Newcomer", null);
        WhatGeneratedImproveResponse mockResponse = new WhatGeneratedImproveResponse();
        mockResponse.improvementResponse().add(new ImprovementResponse("improvementPoint", "advice"));

        when(objectMapper.readValue(message, CreatePromptRequest.class)).thenReturn(promptRequest);

        when(gptService.createdImprovementPointsAndAdvice(any(), any(), any())).thenReturn(mockResponse);

        // When
        resumeFacade.generateAdviceWithKafka(message);

        // Then
        verify(savePredictionUseCase, times(1)).savePrediction(any());
    }


}
