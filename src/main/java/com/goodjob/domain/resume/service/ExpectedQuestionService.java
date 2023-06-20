package com.goodjob.domain.resume.service;

import com.goodjob.domain.resume.domain.ExpectedQuestion;
import com.goodjob.domain.resume.dto.response.WhatGeneratedQuestionResponse;
import com.goodjob.domain.resume.repository.ExpectedQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ExpectedQuestionService {

    private final ExpectedQuestionRepository expectedQuestionRepository;

    @Transactional
    public void saveExpectedQuestion(WhatGeneratedQuestionResponse whatGeneratedQuestionResponse) {
        ExpectedQuestion expectedQuestion = whatGeneratedQuestionResponse.toExpectedQuestion();

        expectedQuestionRepository.save(expectedQuestion);
    }
}