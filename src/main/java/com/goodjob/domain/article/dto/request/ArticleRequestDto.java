package com.goodjob.domain.article.dto.request;

import com.goodjob.domain.hashTag.dto.response.HashTagResponseDto;
import com.goodjob.domain.hashTag.entity.HashTag;
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
    @Size(max=200)
    private String title;
    @NotBlank(message="내용을 작성해주셔야 합니다.")
    private String content;

    private String hashTagStr;
    private List<HashTagResponseDto> hashTags = new ArrayList<>();

    public void setHashTags(List<HashTagResponseDto> hashTags) {
        for(HashTagResponseDto hashTag : hashTags) {
            this.hashTags.add(hashTag);
        }
    }
}
