package com.goodjob.domain.comment.mapper;

import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponseDto toDto(Comment comment);
}