package com.goodjob.domain.comment.controller;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id, @Valid CommentForm commentForm, BindingResult bindingResult) {
        Article article = articleService.getArticle(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "/article/detailArticle";
        }
        commentService.create(article, commentForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }

    @AllArgsConstructor
    @Getter
    public static class CommentForm {
        @NotBlank(message="내용을 작성해주셔야 합니다.")
        private final String content;
    }
}
