package com.goodjob.article.domain.hashTag.mapper;


import com.goodjob.article.domain.hashTag.entity.HashTag;
import com.goodjob.article.domain.hashTag.dto.response.HashTagResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashTagMapper {
    HashTagResponseDto toDto(HashTag hashTag);
}
