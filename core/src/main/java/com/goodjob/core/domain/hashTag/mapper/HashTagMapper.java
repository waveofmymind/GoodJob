package com.goodjob.core.domain.hashTag.mapper;


import com.goodjob.core.domain.hashTag.dto.response.HashTagResponseDto;
import com.goodjob.core.domain.hashTag.entity.HashTag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashTagMapper {
    HashTagResponseDto toDto(HashTag hashTag);
}
