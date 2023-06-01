package com.goodjob.domain.likes.controller;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.controller.CommentController;
import com.goodjob.domain.likes.dto.request.LikesRequestDto;
import com.goodjob.domain.likes.service.LikesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
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

    @PostMapping("/like/article/{id}")
    public String likeArticle(@PathVariable("id") Long id) {
        LikesRequestDto likesRequestDto = new LikesRequestDto(id, null);

        likesService.likeArticle(likesRequestDto);


        return "redirect:/article/detail/%s".formatted(id);
    }

    @PostMapping("/like/comment/{id}")
    public String likeComment(@PathVariable("id") Long id) {
        LikesRequestDto likesRequestDto = new LikesRequestDto(null, id);

        likesService.likeComment(likesRequestDto);


        return "redirect:/article/detail/%s".formatted(id);
    }
}
