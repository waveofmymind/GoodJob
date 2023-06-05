package com.goodjob.domain.comment.service;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.mapper.CommentMapper;
import com.goodjob.domain.comment.repository.CommentRepository;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.entity.SubComment;
import com.goodjob.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ArticleService articleService;

    public RsData createComment(Member member, Long id, CommentRequestDto commentRequestDto) {
        RsData<Article> articleRsData = articleService.getArticle(id);

        if(articleRsData.isFail()) {
            return articleRsData;
        }

        Comment comment = Comment
                .builder()
                .member(member)
                .article(articleRsData.getData())
                .content(commentRequestDto.getContent())
                .isDeleted(false)
                .build();

        commentRepository.save(comment);

        return RsData.of("S-1", "댓글이 작성되었습니다.");
    }

    public RsData getComment(Long id) {
        Optional<Comment> commentOp = commentRepository.findById(id);

        if(commentOp.isEmpty()) {
            return RsData.of("F-1", "해당 댓글이 존재하지 않습니다.");
        }

        Comment comment = commentOp.get();

        if(comment.isDeleted()) {
            return RsData.of("F-2", "해당 댓글은 이미 삭제되었습니다.");
        }

        if(comment.getArticle().isDeleted()) {
            return RsData.of("F-3", "게시글이 이미 삭제되었습니다.");
        }

        return RsData.of("S-1", "댓글에 대한 정보를 가져옵니다.", comment);
    }

    public RsData getCommentResponseDto(Long id) {
        RsData<Comment> commentRsData = getComment(id);

        if(commentRsData.isFail()) {
            return commentRsData;
        }

        Comment comment = commentRsData.getData();

        return RsData.of("S-1", "댓글에 대한 정보를 가져옵니다", commentMapper.toDto(comment));
    }

    public RsData updateComment(Member author, Long id, CommentRequestDto commentRequestDto) {
        RsData<Comment> commentRsData = getComment(id);

        if(commentRsData.isFail()) {
            return commentRsData;
        }

        Comment comment = commentRsData.getData();

        if(!Objects.equals(comment.getMember(), author)) {
            return RsData.of("F-4", "수정 권한이 없습니다.");
        }

        comment.setContent(commentRequestDto.getContent());

        commentRepository.save(comment);

        return RsData.of("S-1", "댓글이 수정되었습니다.", comment);
    }

    @Transactional
    public RsData deleteComment(Member author, Long id) {
        RsData<Comment> commentRsData = getComment(id);

        if(commentRsData.isFail()) {
            return commentRsData;
        }

        Comment comment = commentRsData.getData();

        if(!Objects.equals(comment.getMember(), author)) {
            return RsData.of("F-4", "삭제 권한이 없습니다.");
        }

        List<SubComment> subCommentList = comment.getSubCommentList();

        for(SubComment subComment : subCommentList) {
            subComment.setDeleted(true);
        }

        comment.setDeleted(true);
        commentRepository.save(comment);

        return RsData.of("S-1", "댓글이 삭제되었습니다.", comment);
        }
}
