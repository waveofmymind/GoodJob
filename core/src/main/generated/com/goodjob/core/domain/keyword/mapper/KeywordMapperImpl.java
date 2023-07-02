package com.goodjob.core.domain.keyword.mapper;

import com.goodjob.core.domain.keyword.dto.response.KeyWordResponseDto;
import com.goodjob.core.domain.keyword.entity.Keyword;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-01T23:05:09+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class KeywordMapperImpl implements KeywordMapper {

    @Override
    public KeyWordResponseDto toDto(Keyword keyword) {
        if ( keyword == null ) {
            return null;
        }

        KeyWordResponseDto keyWordResponseDto = new KeyWordResponseDto();

        keyWordResponseDto.setId( keyword.getId() );
        keyWordResponseDto.setContent( keyword.getContent() );

        return keyWordResponseDto;
    }
}
