package com.goodjob.core.domain.subComment.service;


import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.comment.service.CommentService;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.subComment.dto.request.SubCommentRequestDto;
import com.goodjob.core.domain.subComment.entity.SubComment;
import com.goodjob.core.domain.subComment.repository.SubCommentRepository;
import com.goodjob.common.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubCommentService {
    private final SubCommentRepository subCommentRepository;
    private final CommentService commentService;

    public RsData createSubComment(Member member, Long id, SubCommentRequestDto subCommentRequestDto) {
        RsData<Comment> commentRsData = commentService.getComment(id);

        if(commentRsData.isFail()) {
            return commentRsData;
        }

        Comment comment = commentRsData.getData();

        SubComment subComment = SubComment
                .builder()
                .member(member)
                .comment(comment)
                .content(subCommentRequestDto.getContent())
                .isDeleted(false)
                .build();

        subCommentRepository.save(subComment);

        return RsData.of("S-1", "답글이 작성되었습니다.", subComment);
    }

    public RsData getSubComment(Long id) {
        Optional<SubComment> subCommentOp = subCommentRepository.findById(id);

        if(subCommentOp.isEmpty()) {
            return RsData.of("F-1", "해당 답글이 존재하지 않습니다.");
        }

        SubComment subComment = subCommentOp.get();

        if(subComment.isDeleted()) {
            return RsData.of("F-2", "답글이 이미 삭제되었습니다.");
        }

        if(subComment.getComment().isDeleted()) {
            return RsData.of("F-3", "댓글이 이미 삭제되었습니다.");
        }

        if(subComment.getComment().getArticle().isDeleted()) {
            return RsData.of("F-4", "게시글이 이미 삭제되었습니다.");
        }

        return RsData.of("S-1", "답글에 대한 정보를 가져옵니다.", subComment);
    }

    public RsData updateComment(Member author, Long id, SubCommentRequestDto subCommentRequestDto) {
        RsData<SubComment> subCommentRsData = getSubComment(id);

        if(subCommentRsData.isFail()) {
            return subCommentRsData;
        }

        SubComment subComment = subCommentRsData.getData();

        if(subComment.getMember().getId() != author.getId()) {
            return RsData.of("F-5", "수정 권한이 없습니다.");
        }

        subComment.setContent(subCommentRequestDto.getContent());

        subCommentRepository.save(subComment);

        return RsData.of("S-1", "답글이 수정되었습니다.", subComment);
    }

    public RsData deleteSubComment(Member author, Long id) {
        RsData<SubComment> subCommentRsData = getSubComment(id);

        if(subCommentRsData.isFail()) {
            return subCommentRsData;
        }

        SubComment subComment = subCommentRsData.getData();

        if(subComment.getMember().getId() != author.getId()) {
            return RsData.of("F-5", "삭제 권한이 없습니다.");
        }


        subComment.setDeleted(true);
        subCommentRepository.save(subComment);

        return RsData.of("S-1", "답글이 삭제되었습니다.", subComment);
    }
}
