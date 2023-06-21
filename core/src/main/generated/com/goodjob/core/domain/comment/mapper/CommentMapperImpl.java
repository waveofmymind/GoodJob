package com.goodjob.core.domain.comment.mapper;

import com.goodjob.core.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.likes.dto.response.LikesResponseDto;
import com.goodjob.core.domain.likes.entity.Likes;
import com.goodjob.core.domain.subComment.dto.response.SubCommentResponseDto;
import com.goodjob.core.domain.subComment.entity.SubComment;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-21T14:48:57+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponseDto toDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setId( comment.getId() );
        commentResponseDto.setCreatedDate( comment.getCreatedDate() );
        commentResponseDto.setModifiedDate( comment.getModifiedDate() );
        commentResponseDto.setMember( comment.getMember() );
        commentResponseDto.setContent( comment.getContent() );
        commentResponseDto.setLikesList( likesListToLikesResponseDtoList( comment.getLikesList() ) );
        commentResponseDto.setDeleted( comment.isDeleted() );
        commentResponseDto.setSubCommentList( subCommentListToSubCommentResponseDtoList( comment.getSubCommentList() ) );

        return commentResponseDto;
    }

    protected LikesResponseDto likesToLikesResponseDto(Likes likes) {
        if ( likes == null ) {
            return null;
        }

        LikesResponseDto likesResponseDto = new LikesResponseDto();

        likesResponseDto.setId( likes.getId() );
        likesResponseDto.setMember( likes.getMember() );

        return likesResponseDto;
    }

    protected List<LikesResponseDto> likesListToLikesResponseDtoList(List<Likes> list) {
        if ( list == null ) {
            return null;
        }

        List<LikesResponseDto> list1 = new ArrayList<LikesResponseDto>( list.size() );
        for ( Likes likes : list ) {
            list1.add( likesToLikesResponseDto( likes ) );
        }

        return list1;
    }

    protected SubCommentResponseDto subCommentToSubCommentResponseDto(SubComment subComment) {
        if ( subComment == null ) {
            return null;
        }

        SubCommentResponseDto subCommentResponseDto = new SubCommentResponseDto();

        subCommentResponseDto.setId( subComment.getId() );
        subCommentResponseDto.setCreatedDate( subComment.getCreatedDate() );
        subCommentResponseDto.setModifiedDate( subComment.getModifiedDate() );
        subCommentResponseDto.setMember( subComment.getMember() );
        subCommentResponseDto.setContent( subComment.getContent() );
        subCommentResponseDto.setDeleted( subComment.isDeleted() );
        subCommentResponseDto.setLikesList( likesListToLikesResponseDtoList( subComment.getLikesList() ) );

        return subCommentResponseDto;
    }

    protected List<SubCommentResponseDto> subCommentListToSubCommentResponseDtoList(List<SubComment> list) {
        if ( list == null ) {
            return null;
        }

        List<SubCommentResponseDto> list1 = new ArrayList<SubCommentResponseDto>( list.size() );
        for ( SubComment subComment : list ) {
            list1.add( subCommentToSubCommentResponseDto( subComment ) );
        }

        return list1;
    }
}
