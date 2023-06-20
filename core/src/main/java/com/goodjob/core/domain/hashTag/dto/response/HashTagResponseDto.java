package com.goodjob.core.domain.hashTag.dto.response;

import com.goodjob.core.domain.keyword.dto.response.KeyWordResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HashTagResponseDto {
    private Long id;
    private KeyWordResponseDto keyword;
}
