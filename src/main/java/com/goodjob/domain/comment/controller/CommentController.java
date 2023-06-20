package com.goodjob.domain.comment.controller;

import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.service.CommentService;
import com.goodjob.global.base.rq.Rq;
import com.goodjob.global.base.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final Rq rq;

    @PostMapping("/create/{id}")
    public String createComment(@PathVariable("id") Long id, @Valid CommentRequestDto commentRequestDto) {

        RsData<Comment> commentRsData = commentService.createComment(rq.getMember(), id, commentRequestDto);

        if(commentRsData.isFail()) {
            return rq.historyBack(commentRsData);
        }

        return "redirect:/article/detail/%s".formatted(id);
    }


    @PostMapping("/update/{id}")
    public String updateComment(@Valid CommentRequestDto commentRequestDto, @PathVariable("id") Long id) {
        RsData<Comment> updateRsData = commentService.updateComment(rq.getMember(), id, commentRequestDto);

        if(updateRsData.isFail()) {
            return rq.historyBack(updateRsData);
        }

        Comment comment = updateRsData.getData();

        return rq.redirectWithMsg("/article/detail/%s".formatted(comment.getArticle().getId()), updateRsData);
    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        RsData<Comment> commentRsData = commentService.deleteComment(rq.getMember(), id);

        if(commentRsData.isFail()) {
            return rq.historyBack(commentRsData);
        }

        Comment comment = commentRsData.getData();

        return rq.redirectWithMsg("/article/detail/%s".formatted(comment.getArticle().getId()), commentRsData);
    }
}
