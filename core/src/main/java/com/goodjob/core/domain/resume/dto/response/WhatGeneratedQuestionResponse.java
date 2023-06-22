package com.goodjob.core.domain.resume.dto.response;


import com.goodjob.core.domain.resume.domain.Answers;
import com.goodjob.core.domain.resume.domain.ExpectedQuestion;
import com.goodjob.core.domain.resume.domain.Questions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record WhatGeneratedQuestionResponse(
        List<PredictionResponse> predictionResponse

) {
    public WhatGeneratedQuestionResponse() {
        this(new ArrayList<>());
    }

    public ExpectedQuestion toExpectedQuestion() {
        List<String> questionsList = predictionResponse.stream()
                .map(PredictionResponse::question)
                .collect(Collectors.toList());

        List<String> answersList = predictionResponse.stream()
                .map(PredictionResponse::bestAnswer)
                .collect(Collectors.toList());

        Questions questions = Questions.of(questionsList);
        Answers answers = Answers.of(answersList);

        return ExpectedQuestion.builder()
                .questions(questions)
                .answers(answers)
                .build();
    }

}

