package com.goodjob.domain.article.mapper;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.likes.mapper.LikesMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LikesMapper.class)
public interface ArticleMapper {
    ArticleResponseDto toDto(Article article);
}
