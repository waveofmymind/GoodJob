package com.goodjob.article.domain.hashTag.dto.response;

import com.goodjob.article.domain.keyword.dto.response.KeyWordResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class HashTagResponseDto implements Serializable {
    private Long id;
    private KeyWordResponseDto keyword;
}
