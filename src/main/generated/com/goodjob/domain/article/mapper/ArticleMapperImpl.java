package com.goodjob.domain.article.mapper;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-31T00:13:58+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
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
        articleResponseDto.setCreateDate( article.getCreateDate() );
        articleResponseDto.setModifiedDate( article.getModifiedDate() );
        articleResponseDto.setTitle( article.getTitle() );
        articleResponseDto.setContent( article.getContent() );
        articleResponseDto.setLikeCount( article.getLikeCount() );
        articleResponseDto.setViewCount( article.getViewCount() );
        articleResponseDto.setCommentList( toCommentDtoList( article.getCommentList() ) );

        return articleResponseDto;
    }

    @Override
    public List<CommentResponseDto> toCommentDtoList(List<Comment> commentList) {
        if ( commentList == null ) {
            return null;
        }

        List<CommentResponseDto> list = new ArrayList<CommentResponseDto>( commentList.size() );
        for ( Comment comment : commentList ) {
            list.add( commentToCommentResponseDto( comment ) );
        }

        return list;
    }

    protected CommentResponseDto commentToCommentResponseDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setId( comment.getId() );
        commentResponseDto.setCreateDate( comment.getCreateDate() );
        commentResponseDto.setModifiedDate( comment.getModifiedDate() );
        commentResponseDto.setMember( comment.getMember() );
        commentResponseDto.setArticle( comment.getArticle() );
        commentResponseDto.setContent( comment.getContent() );
        commentResponseDto.setLikeCount( comment.getLikeCount() );
        commentResponseDto.setDeleted( comment.isDeleted() );

        return commentResponseDto;
    }
}
