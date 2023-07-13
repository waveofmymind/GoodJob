package com.goodjob.article.domain.comment.service;

import com.goodjob.article.domain.article.entity.Article;
import com.goodjob.article.domain.article.service.ArticleService;
import com.goodjob.article.domain.comment.mapper.CommentMapper;
import com.goodjob.article.domain.comment.repository.CommentRepository;
import com.goodjob.article.domain.subComment.entity.SubComment;
import com.goodjob.article.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.article.domain.comment.entity.Comment;
import com.goodjob.common.rsData.RsData;
import com.goodjob.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ArticleService articleService;

    public RsData createComment(Member member, Long id, CommentRequestDto commentRequestDto) {
        RsData<Article> articleRsData = articleService.getArticle(id);

        if (articleRsData.isFail()) {
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

    @Transactional
    public RsData getComment(Long id) {
        Optional<Comment> commentOp = commentRepository.findById(id);

        if (commentOp.isEmpty()) {
            return RsData.of("F-1", "해당 댓글이 존재하지 않습니다.");
        }

        Comment comment = commentOp.get();

        if (comment.isDeleted()) {
            return RsData.of("F-2", "해당 댓글은 이미 삭제되었습니다.");
        }

        if (comment.getArticle().isDeleted()) {
            return RsData.of("F-3", "게시글이 이미 삭제되었습니다.");
        }

        return RsData.of("S-1", "댓글에 대한 정보를 가져옵니다.", comment);
    }

    @Transactional
    public RsData updateComment(Member author, Long id, CommentRequestDto commentRequestDto) {
        RsData<Comment> commentRsData = getComment(id);

        if (commentRsData.isFail()) {
            return commentRsData;
        }

        Comment comment = commentRsData.getData();

        if (comment.getMember().getId() != author.getId()) {
            return RsData.of("F-4", "수정 권한이 없습니다.");
        }

        comment.setContent(commentRequestDto.getContent());

        return RsData.of("S-1", "댓글이 수정되었습니다.", comment);
    }

    @Transactional
    public RsData deleteComment(Member author, Long id) {
        RsData<Comment> commentRsData = getComment(id);

        if (commentRsData.isFail()) {
            return commentRsData;
        }

        Comment comment = commentRsData.getData();

        if (comment.getMember().getId() != author.getId()) {
            return RsData.of("F-4", "삭제 권한이 없습니다.");
        }

        List<SubComment> subCommentList = comment.getSubCommentList();

        for (SubComment subComment : subCommentList) {
            subComment.setDeleted(true);
        }

        comment.setDeleted(true);

        return RsData.of("S-1", "댓글이 삭제되었습니다.", comment);
    }

    public List<Comment> findAllByMemberId(Long memberId) {
        return commentRepository.findAllByMemberIdOrderByCreatedDateDesc(memberId);
    }

    public Page<Comment> findAllByMemberIdToPage(int page, Long memberId) {
        Pageable pageable = PageRequest.of(page, 10);

        List<Comment> comments = commentRepository.findAllByMemberIdOrderByCreatedDateDesc(memberId);

        return convertToPage(comments, pageable);
    }

    private Page<Comment> convertToPage(List<Comment> comments, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), comments.size());

        List<Comment> content = comments.subList(start, end);
        return new PageImpl<>(content, pageable, comments.size());
    }

}
