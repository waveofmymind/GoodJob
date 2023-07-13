package com.goodjob.article.domain.hashTag.dto.response;

import com.goodjob.article.domain.keyword.dto.response.KeyWordResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HashTagResponseDto {
    private Long id;
    private KeyWordResponseDto keyword;
}
