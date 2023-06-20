package com.goodjob.core.domain.article.mapper;



import com.goodjob.core.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.core.domain.article.entity.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleResponseDto toDto(Article article);

}
