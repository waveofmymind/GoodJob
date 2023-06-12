package com.goodjob.domain.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.goodjob.domain.resume.dto.request.ResumeRequest;
import com.goodjob.domain.resume.dto.response.WhatGeneratedImproveResponse;
import com.goodjob.domain.resume.dto.response.WhatGeneratedQuestionResponse;
import com.goodjob.global.error.ErrorCode;
import com.goodjob.global.error.exception.BusinessException;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.goodjob.global.config.GptConfig;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


import org.springframework.stereotype.Service;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public ChatCompletionResult generate(List<ChatMessage> chatMessages) {

        ChatCompletionRequest build = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .maxTokens(GptConfig.MAX_TOKEN)
                .temperature(GptConfig.TEMPERATURE)
                .topP(GptConfig.TOP_P)
                .model(GptConfig.MODEL)
                .build();

        return openAiService.createChatCompletion(build);

    }

    public List<ChatMessage> generateQuestionMessage(String job, String career, String resumeType, String content) {

        final String prompt = Prompt.generateQuestionPrompt(job, career, resumeType);


        log.debug("생성된 프롬프트 : {} ", prompt);

        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), prompt);
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), content);

        return List.of(systemMessage, userMessage);
    }

    public List<ChatMessage> generateAdviceMessage(String job, String career, String resumeType, String content) {

        final String prompt = Prompt.generateAdvicePrompt(job, career, resumeType);


        log.debug("생성된 프롬프트 : {} ", prompt);

        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), prompt);
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), content);

        return List.of(systemMessage, userMessage);
    }

    public WhatGeneratedQuestionResponse createdExpectedQuestionsAndAnswer(String job, String career, List<ResumeRequest> resumeData) {

        // 멀티스레드 호출
        List<CompletableFuture<WhatGeneratedQuestionResponse>> futures = new ArrayList<>();

        for (ResumeRequest data : resumeData) {
            CompletableFuture<WhatGeneratedQuestionResponse> future = CompletableFuture.supplyAsync(() -> {
                List<ChatMessage> chatMessages = generateQuestionMessage(job, career, data.resumeType(), data.content());
                try {
                    ChatCompletionResult chatCompletionResult = generate(chatMessages);

                    String futureResult = chatCompletionResult.getChoices().get(0).getMessage().getContent();


                    return objectMapper.readValue(futureResult, WhatGeneratedQuestionResponse.class);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                    throw new BusinessException(ErrorCode.JSON_PARSING_FAILED);
                }
            }, executorService);

            futures.add(future);
        }
        log.info(futures.toString());
            // 모든 CompletableFuture가 완료될 때까지 대기
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            try {
                allFutures.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage());
                throw new BusinessException(ErrorCode.THREAD_MALFUNCTION);
            }

            // CompletableFuture에서 결과를 추출해서 WhatGeneratedResponse 객체에 저장
            WhatGeneratedQuestionResponse result = new WhatGeneratedQuestionResponse();
            futures.stream()
                    .map(CompletableFuture::join)
                    .filter(content -> content.predictionResponse().size() != 0)
                    .forEach(content -> result.predictionResponse().addAll(content.predictionResponse()));

            return result;
        }

    public WhatGeneratedImproveResponse createdImprovementPointsAndAdvice(String job, String career, List<ResumeRequest> resumeData) {

        // 멀티스레드 호출
        List<CompletableFuture<WhatGeneratedImproveResponse>> futures = new ArrayList<>();

        for (ResumeRequest data : resumeData) {
            CompletableFuture<WhatGeneratedImproveResponse> future = CompletableFuture.supplyAsync(() -> {
                List<ChatMessage> chatMessages = generateAdviceMessage(job, career, data.resumeType(), data.content());
                try {
                    ChatCompletionResult chatCompletionResult = generate(chatMessages);

                    String futureResult = chatCompletionResult.getChoices().get(0).getMessage().getContent();


                    return objectMapper.readValue(futureResult, WhatGeneratedImproveResponse.class);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                    throw new BusinessException(ErrorCode.JSON_PARSING_FAILED);
                }
            }, executorService);

            futures.add(future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            allFutures.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorCode.THREAD_MALFUNCTION);
        }

        WhatGeneratedImproveResponse result = new WhatGeneratedImproveResponse();
        futures.stream()
                .map(CompletableFuture::join)
                .filter(content -> content.improvementResponse().size() != 0)
                .forEach(content -> result.improvementResponse().addAll(content.improvementResponse()));

        return result;
    }

}