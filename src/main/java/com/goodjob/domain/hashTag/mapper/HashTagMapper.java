package com.goodjob.domain.hashTag.mapper;

import com.goodjob.domain.hashTag.dto.response.HashTagResponseDto;
import com.goodjob.domain.hashTag.entity.HashTag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashTagMapper {
    HashTagResponseDto toDto(HashTag hashTag);
}
