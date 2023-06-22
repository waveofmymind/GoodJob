package com.goodjob.core.domain.resume.mapper;


import com.goodjob.core.domain.resume.dto.request.ResumeRequest;
import com.goodjob.core.global.error.ErrorCode;
import com.goodjob.core.global.error.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenerateExpectedMapper {

    public List<ResumeRequest> toResumeData(List<String> resumeType, List<String> content) {
        List<ResumeRequest> resumeRequests = new ArrayList<>();

        if (resumeType != null && content != null && resumeType.size() == content.size()) {
            for (int i = 0; i < resumeType.size(); i++) {
                String type = resumeType.get(i);
                String contentText = content.get(i);

                ResumeRequest resumeRequest = ResumeRequest.builder()
                        .resumeType(type)
                        .content(contentText)
                        .build();

                resumeRequests.add(resumeRequest);
            }
        } else {
            // 길이가 다르면 예외를 던지거나 적절하게 처리
            throw new BusinessException(ErrorCode.CREATE_PREDICTION_QUESTION);
        }

        return resumeRequests;
    }
}
