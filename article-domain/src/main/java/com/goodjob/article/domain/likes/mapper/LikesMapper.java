package com.goodjob.article.domain.likes.mapper;


import com.goodjob.article.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.article.domain.comment.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LikesMapper {

    List<CommentResponseDto> toCommentDtoList(List<Comment> commentList);
}
