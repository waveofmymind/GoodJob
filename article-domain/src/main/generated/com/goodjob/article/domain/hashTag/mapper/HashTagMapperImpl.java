package com.goodjob.article.domain.hashTag.mapper;

import com.goodjob.article.domain.hashTag.dto.response.HashTagResponseDto;
import com.goodjob.article.domain.hashTag.entity.HashTag;
import com.goodjob.article.domain.keyword.dto.response.KeyWordResponseDto;
import com.goodjob.article.domain.keyword.entity.Keyword;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-13T14:16:07+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class HashTagMapperImpl implements HashTagMapper {

    @Override
    public HashTagResponseDto toDto(HashTag hashTag) {
        if ( hashTag == null ) {
            return null;
        }

        HashTagResponseDto hashTagResponseDto = new HashTagResponseDto();

        hashTagResponseDto.setId( hashTag.getId() );
        hashTagResponseDto.setKeyword( keywordToKeyWordResponseDto( hashTag.getKeyword() ) );

        return hashTagResponseDto;
    }

    protected KeyWordResponseDto keywordToKeyWordResponseDto(Keyword keyword) {
        if ( keyword == null ) {
            return null;
        }

        KeyWordResponseDto keyWordResponseDto = new KeyWordResponseDto();

        keyWordResponseDto.setId( keyword.getId() );
        keyWordResponseDto.setContent( keyword.getContent() );

        return keyWordResponseDto;
    }
}
