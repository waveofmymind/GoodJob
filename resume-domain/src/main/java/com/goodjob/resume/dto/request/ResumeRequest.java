package com.goodjob.resume.dto.request;

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
}
