package com.goodjob.article.domain.comment.mapper;


import com.goodjob.article.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.article.domain.comment.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponseDto toDto(Comment comment);
}