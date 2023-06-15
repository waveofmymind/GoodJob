package com.goodjob.domain.resume.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.domain.resume.dto.request.ResumeRequest;
import com.goodjob.global.error.ErrorCode;
import com.goodjob.global.error.exception.BusinessException;

import java.beans.PropertyEditorSupport;

public class ResumeRequestEditor extends PropertyEditorSupport {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setAsText(String input) {
        try {
            ResumeRequest resumeRequest = objectMapper.readValue(input, ResumeRequest.class);
            setValue(resumeRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.JSON_PARSING_FAILED);
        }
    }
}


