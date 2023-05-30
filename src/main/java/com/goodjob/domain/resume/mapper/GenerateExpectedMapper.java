package com.goodjob.domain.resume.mapper;

import com.goodjob.domain.resume.dto.request.ResumeRequest;
import org.springframework.stereotype.Component;

@Component
public class GenerateExpectedMapper {

    public ResumeRequest toResumeData(String resumeType, String content) {
        return ResumeRequest.builder()
                .resumeType(resumeType)
                .content(content).build();
    }
}
