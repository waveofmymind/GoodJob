package com.goodjob.domain.comment.controller;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.service.CommentService;
import com.goodjob.global.base.rq.Rq;
import com.goodjob.global.base.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;
    private final Rq rq;

    //TODO: 필요없는 메서드인지 확인 후 삭제
//    @GetMapping("/get/{id}")
//    public String redirectToArticleWithComment(@PathVariable("id") Long id) {
//        Article article = commentService.getComment(id).getArticle();
//
//        return "redirect:/article/detail/%s".formatted(article.getId());
//    }

    @PostMapping("/create/{id}")
    public String createComment(@PathVariable("id") Long id, @Valid CommentRequestDto commentRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "article/detailArticle";
        }
        RsData<Comment> commentRsData = commentService.createComment(rq.getMember(), id, commentRequestDto);

        if(commentRsData.isFail()) {
            return rq.historyBack(commentRsData);
        }

        return rq.redirectWithMsg("/article/detail/%s".formatted(id), commentRsData);
    }


    @PostMapping("/update/{id}")
    public String updateComment(@Valid CommentRequestDto commentRequestDto, BindingResult bindingResult,
                               @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "/article/detailArticle";
        }

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
