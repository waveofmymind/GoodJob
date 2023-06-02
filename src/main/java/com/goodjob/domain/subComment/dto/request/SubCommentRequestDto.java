package com.goodjob.domain.subComment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCommentRequestDto {
    @NotBlank(message = "내용을 작성해주셔야 합니다.")
    private String content;

}
