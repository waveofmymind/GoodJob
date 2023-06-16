package com.goodjob.domain.article.mapper;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.hashTag.dto.response.HashTagResponseDto;
import com.goodjob.domain.hashTag.entity.HashTag;
import com.goodjob.domain.keyword.dto.response.KeyWordResponseDto;
import com.goodjob.domain.keyword.entity.Keyword;
import com.goodjob.domain.likes.dto.response.LikesResponseDto;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.subComment.dto.response.SubCommentResponseDto;
import com.goodjob.domain.subComment.entity.SubComment;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-15T16:54:22+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
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
        articleResponseDto.setCreatedDate( article.getCreatedDate() );
        articleResponseDto.setModifiedDate( article.getModifiedDate() );
        articleResponseDto.setTitle( article.getTitle() );
        articleResponseDto.setContent( article.getContent() );
        articleResponseDto.setLikesList( likesListToLikesResponseDtoList( article.getLikesList() ) );
        articleResponseDto.setViewCount( article.getViewCount() );
        articleResponseDto.setCommentList( commentListToCommentResponseDtoList( article.getCommentList() ) );
        articleResponseDto.setCommentsCount( article.getCommentsCount() );
        articleResponseDto.setMember( article.getMember() );
        articleResponseDto.setHashTagList( hashTagListToHashTagResponseDtoList( article.getHashTagList() ) );

        return articleResponseDto;
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

    protected CommentResponseDto commentToCommentResponseDto(Comment comment) {
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

    protected List<CommentResponseDto> commentListToCommentResponseDtoList(List<Comment> list) {
        if ( list == null ) {
            return null;
        }

        List<CommentResponseDto> list1 = new ArrayList<CommentResponseDto>( list.size() );
        for ( Comment comment : list ) {
            list1.add( commentToCommentResponseDto( comment ) );
        }

        return list1;
    }

    protected KeyWordResponseDto keywordToKeyWordResponseDto(Keyword keyword) {
        if ( keyword == null ) {
            return null;
        }

        KeyWordResponseDto keyWordResponseDto = new KeyWordResponseDto();

        keyWordResponseDto.setId( keyword.getId() );
        keyWordResponseDto.setContent( keyword.getContent() );

        return keyWordResponseDto;
    }

    protected HashTagResponseDto hashTagToHashTagResponseDto(HashTag hashTag) {
        if ( hashTag == null ) {
            return null;
        }

        HashTagResponseDto hashTagResponseDto = new HashTagResponseDto();

        hashTagResponseDto.setId( hashTag.getId() );
        hashTagResponseDto.setKeyword( keywordToKeyWordResponseDto( hashTag.getKeyword() ) );

        return hashTagResponseDto;
    }

    protected List<HashTagResponseDto> hashTagListToHashTagResponseDtoList(List<HashTag> list) {
        if ( list == null ) {
            return null;
        }

        List<HashTagResponseDto> list1 = new ArrayList<HashTagResponseDto>( list.size() );
        for ( HashTag hashTag : list ) {
            list1.add( hashTagToHashTagResponseDto( hashTag ) );
        }

        return list1;
    }
}
