package com.goodjob.domain.keyword.mapper;

import com.goodjob.domain.keyword.dto.response.KeyWordResponseDto;
import com.goodjob.domain.keyword.entity.Keyword;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KeywordMapper {
    KeyWordResponseDto toDto(Keyword keyword);
}
