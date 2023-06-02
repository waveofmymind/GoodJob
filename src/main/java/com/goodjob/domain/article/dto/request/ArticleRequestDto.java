package com.goodjob.domain.article.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequestDto {
    @NotBlank(message="제목을 작성해주셔야 합니다.")
    @Size(max=200)
    private String title;
    @NotBlank(message="내용을 작성해주셔야 합니다.")
    private String content;
}
