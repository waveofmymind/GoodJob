package com.goodjob.article.domain.article.mapper;



import com.goodjob.article.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.article.domain.article.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "extra", ignore = true)
    ArticleResponseDto toDto(Article article);

}
