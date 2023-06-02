package com.goodjob.domain.likes.controller;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.controller.CommentController;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.service.CommentService;
import com.goodjob.domain.likes.dto.request.LikesRequestDto;
import com.goodjob.domain.likes.service.LikesService;
import com.goodjob.domain.subComment.entity.SubComment;
import com.goodjob.domain.subComment.service.SubCommentService;
import com.goodjob.global.base.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {

    private final LikesService likesService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like/article/{id}")
    public String likeArticle(@PathVariable("id") Long id) {
        LikesRequestDto likesRequestDto = new LikesRequestDto(id, null, null);

        likesService.likeArticle(rq.getMember(), likesRequestDto);


        return "redirect:/article/detail/%s".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like/comment/{id}")
    public String likeComment(@PathVariable("id") Long id) {
        LikesRequestDto likesRequestDto = new LikesRequestDto(null, id, null);

        Long articleId = likesService.likeComment(rq.getMember(), likesRequestDto);


        return "redirect:/article/detail/%s".formatted(articleId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like/subComment/{id}")
    public String likeSubComment(@PathVariable("id") Long id) {
        LikesRequestDto likesRequestDto = new LikesRequestDto(null, null, id);

        Long articleId = likesService.likeSubComment(rq.getMember(), likesRequestDto);


        return "redirect:/article/detail/%s".formatted(articleId);
    }
}
