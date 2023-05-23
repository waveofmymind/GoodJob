package com.goodjob.domain.gpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import com.goodjob.global.config.GptConfig;

import org.springframework.stereotype.Service;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

    @Value("${openai.token}")
    private String OPENAI_TOKEN;

    private final ObjectMapper objectMapper;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public ChatCompletionResult generate(List<ChatMessage> chatMessages) {
        OpenAiService openAiService = new OpenAiService(OPENAI_TOKEN, GptConfig.TIME_OUT);

        ChatCompletionRequest build = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .maxTokens(GptConfig.MAX_TOKEN)
                .temperature(GptConfig.TEMPERATURE)
                .topP(GptConfig.TOP_P)
                .model(GptConfig.MODEL)
                .build();

        return openAiService.createChatCompletion(build);

    }
}
