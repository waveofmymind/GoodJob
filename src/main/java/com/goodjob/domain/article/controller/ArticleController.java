package com.goodjob.domain.article.controller;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.repository.ArticleRepository;
import com.goodjob.domain.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/list")
    public String list(Model model) {
        List<ArticleResponseDto> articleResponseDtoList = this.articleService.getList();
        model.addAttribute(articleResponseDtoList);
        return "/article/list";
    }

    @GetMapping("/detail/{id}")
    public String detail (Model model, @PathVariable("id") Long id) {
        return "/article/detailArticle";

    }
}
