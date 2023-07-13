package com.goodjob.mentoring.domain.mentoring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentoringRequestDto {
    @NotBlank(message="제목을 작성해주셔야 합니다.")
    @Size(max=30)
    private String title;
    @NotBlank(message="내용을 작성해주셔야 합니다.")
    private String content;
    private String currentJob;
    private String job;
    private String career;
    private String preferredTime;
}
