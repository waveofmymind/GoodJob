package com.goodjob.core.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    @NotBlank(message="내용을 작성해주셔야 합니다.")
    private String content;

}
