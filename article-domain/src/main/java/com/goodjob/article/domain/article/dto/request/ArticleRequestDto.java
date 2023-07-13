package com.goodjob.article.domain.article.dto.request;

import com.goodjob.article.domain.hashTag.dto.response.HashTagResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArticleRequestDto {
    @NotBlank(message="제목을 작성해주셔야 합니다.")
    @Size(max=30)
    private String title;
    @NotBlank(message="내용을 작성해주셔야 합니다.")
    private String content;
    @Size(max=10)
    private String hashTagStr;
    private List<HashTagResponseDto> hashTags = new ArrayList<>();
    private int category;
}
