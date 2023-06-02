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

    @GetMapping("/get/{id}")
    public String redirectToArticleWithComment(@PathVariable("id") Long id) {
        Article article = commentService.getComment(id).getArticle();

        return "redirect:/article/detail/%s".formatted(article.getId());
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id, @Valid CommentRequestDto commentRequestDto, BindingResult bindingResult) {
        RsData<Article> articleRsData = articleService.getArticle(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", articleRsData.getData());
            return "article/detailArticle";
        }
        commentService.createComment(rq.getMember(), articleRsData.getData(), commentRequestDto);

        return String.format("redirect:/article/detail/%s", id);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String updateComment(@Valid CommentRequestDto commentRequestDto, BindingResult bindingResult,
                               @PathVariable("id") Long id, Principal principal) {
//        if (bindingResult.hasErrors()) {
//            return "answer_form";
//        }
        Comment comment = commentService.getComment(id);
//        if (!commentResponseDto.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        commentService.updateComment(comment, commentRequestDto.getContent());
        return String.format("redirect:/article/detail/%s", comment.getArticle().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteComment(Principal principal, @PathVariable("id") Long id) {
        Comment comment = commentService.getComment(id);
//        if (!commentResponseDto.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        commentService.deleteComment(comment);
        return String.format("redirect:/article/detail/%s", comment.getArticle().getId());
    }
}
