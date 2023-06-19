package com.goodjob.domain.resume.dto.response;


import com.goodjob.domain.resume.domain.Answers;
import com.goodjob.domain.resume.domain.ExpectedQuestion;
import com.goodjob.domain.resume.domain.Questions;

import java.util.ArrayList;
import java.util.List;

public record WhatGeneratedQuestionResponse(
        List<PredictionResponse> predictionResponse

) {
    public WhatGeneratedQuestionResponse() {
        this(new ArrayList<>());
    }

    public ExpectedQuestion toExpectedQuestion() {
        // Questions와 Answers 객체를 생성
        List<String> questionsList = new ArrayList<>();
        List<String> answersList = new ArrayList<>();
        for (PredictionResponse response : predictionResponse) {
            questionsList.add(response.question());
            answersList.add(response.bestAnswer());
        }
        Questions questions = Questions.of(questionsList);
        questions.getQuestions().addAll(questionsList);

        Answers answers = Answers.of(answersList);
        answers.getAnswers().addAll(answersList);

        return ExpectedQuestion.builder()
                .questions(questions)
                .answers(answers)
                .build();
    }
}

