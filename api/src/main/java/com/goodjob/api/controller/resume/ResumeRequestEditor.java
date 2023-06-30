package com.goodjob.api.controller.resume;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.resume.dto.request.ResumeRequest;
import com.goodjob.core.global.error.ErrorCode;
import com.goodjob.core.global.error.exception.BusinessException;


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
