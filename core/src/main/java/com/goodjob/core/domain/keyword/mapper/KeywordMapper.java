package com.goodjob.core.domain.keyword.mapper;


import com.goodjob.core.domain.keyword.dto.response.KeyWordResponseDto;
import com.goodjob.core.domain.keyword.entity.Keyword;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KeywordMapper {
    KeyWordResponseDto toDto(Keyword keyword);
}
