package com.goodjob.article.domain.keyword.mapper;


import com.goodjob.article.domain.keyword.dto.response.KeyWordResponseDto;
import com.goodjob.article.domain.keyword.entity.Keyword;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KeywordMapper {
    KeyWordResponseDto toDto(Keyword keyword);
}
