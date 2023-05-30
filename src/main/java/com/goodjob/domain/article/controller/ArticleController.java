package com.goodjob.domain.article.controller;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.controller.CommentController;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/main")
    public String main(Model model) {
        List<ArticleResponseDto> articleList = articleService.findAll();

        model.addAttribute("articleList", articleList);

        return "/article/main";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<ArticleResponseDto> articleList = articleService.findAll();

        model.addAttribute("articleList", articleList);

        return "/article/list";
    }

    @GetMapping("/detail/{id}")
    public String detail (Model model, @PathVariable("id") Long id, CommentController.CommentForm commentForm) {
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

    @Getter
    @Setter
    public static class ArticleForm {
        @NotBlank(message="제목을 작성해주셔야 합니다.")
        @Size(max=200)
        private String title;
        @NotBlank(message="내용을 작성해주셔야 합니다.")
        private String content;
    }


//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String articleModify(ArticleForm articleForm, @PathVariable("id") Long id, Principal principal) {
        ArticleResponseDto articleResponseDto = articleService.getArticleResponseDto(id);
//        if(!article.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        articleForm.setTitle(articleResponseDto.getTitle());
        articleForm.setContent(articleResponseDto.getContent());
        return "/article/articleForm";
    }

//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String articleModify(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "/article/articleForm";
        }
        Article article = articleService.getArticle(id);
//        if (!articleResponseDto.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        articleService.modify(article, articleForm.getTitle(), articleForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String articleDelete(Principal principal, @PathVariable("id") Long id) {
        Article article = articleService.getArticle(id);
//        if (!article.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
//        }
        articleService.delete(article);
        return "redirect:/article/list";
    }
}
