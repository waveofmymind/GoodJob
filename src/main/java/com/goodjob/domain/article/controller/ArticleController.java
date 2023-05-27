package com.goodjob.domain.article.controller;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.repository.ArticleRepository;
import com.goodjob.domain.article.service.ArticleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/list")
    public String list(Model model) {
        List<ArticleResponseDto> articleList = articleService.findAll();

        model.addAttribute("articleList", articleList);

        return "/article/list";
    }

    @GetMapping("/detail/{id}")
    public String detail (Model model, @PathVariable("id") Long id) {
        ArticleResponseDto articleResponseDto = articleService.getArticleResponseDto(id);
        model.addAttribute("article", articleResponseDto);
        return "/article/detailArticle";

    }

    @GetMapping("/create")
    public String articleCreate(ArticleForm articleForm) {
        return "/article/articleForm";
    }

    @PostMapping("/create")
    public String articleCreate(@Valid ArticleForm articleForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/article/articleForm";
        }
        articleService.create(articleForm.getTitle(), articleForm.getContent());
        return "redirect:/article/list";
    }

    @AllArgsConstructor
    @Getter
    public static class ArticleForm {
        @NotBlank(message="제목을 작성해주셔야 합니다.")
        @Size(max=200)
        private final String title;
        @NotBlank(message="내용을 작성해주셔야 합니다.")
        private final String content;
    }


}
