package com.goodjob.core.domain.comment.mapper;


import com.goodjob.core.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.core.domain.comment.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponseDto toDto(Comment comment);
}