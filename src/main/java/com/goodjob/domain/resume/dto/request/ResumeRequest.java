package com.goodjob.domain.resume.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record ResumeRequest(
        @NotBlank
        String resumeType,

        @NotBlank
        String content
) {
        @Builder
        public ResumeRequest {
        }
}