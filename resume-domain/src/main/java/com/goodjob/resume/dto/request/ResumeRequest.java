package com.goodjob.resume.dto.request;

import com.goodjob.resume.domain.resume.ResumeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeRequest{
        @NotBlank
        String resumeType;

        @NotBlank
        String content;

        public ResumeInfo toEntity() {
            return ResumeInfo.builder()
                    .resumeType(resumeType)
                    .content(content)
                    .build();
        }
}
