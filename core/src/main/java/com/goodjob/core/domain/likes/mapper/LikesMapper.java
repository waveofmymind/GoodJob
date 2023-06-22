package com.goodjob.core.domain.likes.mapper;

import com.goodjob.core.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.core.domain.comment.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LikesMapper {

    List<CommentResponseDto> toCommentDtoList(List<Comment> commentList);
}
