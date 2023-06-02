package com.goodjob.domain.subComment.service;

import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.dto.request.SubCommentRequestDto;
import com.goodjob.domain.subComment.entity.SubComment;
import com.goodjob.domain.subComment.repository.SubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubCommentService {
    private final SubCommentRepository subCommentRepository;

    public void createSubComment(Member member, Comment comment, SubCommentRequestDto subCommentRequestDto) {
        SubComment subComment = SubComment
                .builder()
                .member(member)
                .comment(comment)
                .content(subCommentRequestDto.getContent())
                .isDeleted(false)
                .build();

        subCommentRepository.save(subComment);
    }

    public SubComment getSubComment(Long id) {
        return subCommentRepository.findById(id).orElseThrow();
    }

    public void updateComment(SubComment subComment, String content) {
        subComment.setContent(content);
        subCommentRepository.save(subComment);
    }

    public void deleteSubComment(SubComment subComment) {
        subComment.setDeleted(true);
        subCommentRepository.save(subComment);
    }
}
