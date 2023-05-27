package com.goodjob.domain.article.mapper;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-27T20:01:14+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class ArticleMapperImpl implements ArticleMapper {

    @Override
    public ArticleResponseDto toDto(Article article) {
        if ( article == null ) {
            return null;
        }

        ArticleResponseDto articleResponseDto = new ArticleResponseDto();

        articleResponseDto.setId( article.getId() );
        articleResponseDto.setCreateDate( article.getCreateDate() );
        articleResponseDto.setModifiedDate( article.getModifiedDate() );
        articleResponseDto.setTitle( article.getTitle() );
        articleResponseDto.setContent( article.getContent() );
        articleResponseDto.setLikeCount( article.getLikeCount() );
        articleResponseDto.setViewCount( article.getViewCount() );

        return articleResponseDto;
    }
}
