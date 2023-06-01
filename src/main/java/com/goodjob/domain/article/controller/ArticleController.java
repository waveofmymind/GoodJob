package com.goodjob.domain.article.controller;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.controller.CommentController;
import com.goodjob.domain.subComment.controller.SubCommentController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/main")
    public String main(Model model) {
        Page<ArticleResponseDto> paging = articleService.findTopFive();

        model.addAttribute("paging", paging);

        return "article/main";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<ArticleResponseDto> paging = articleService.findAll(page);
        model.addAttribute("paging", paging);

        return "article/list";
    }

    @GetMapping("/detail/{id}")
    public String detailArticle (Model model, @PathVariable("id") Long id, CommentController.CommentForm commentForm, SubCommentController.SubCommentForm subCommentForm) {
        Article article = articleService.getArticle(id);
        ArticleResponseDto articleResponseDto = articleService.increaseViewCount(article);
        model.addAttribute("article", articleResponseDto);
        return "article/detailArticle";

    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createArticle(ArticleForm articleForm) {
        return "article/articleForm";
    }

    @PostMapping("/create")
    public String createArticle(@Valid ArticleForm articleForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "article/articleForm";
        }
        ArticleRequestDto articleRequestDto = new ArticleRequestDto(articleForm.getTitle(), articleForm.getContent());
        articleService.createArticle(articleRequestDto);
        long id = articleService.getCreatedArticleId();
        return "redirect:/article/detail/%s".formatted(id);
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
    @GetMapping("/update/{id}")
    public String updateArticle(ArticleForm articleForm, @PathVariable("id") Long id, Principal principal) {
        ArticleResponseDto articleResponseDto = articleService.getArticleResponseDto(id);
//        if(!article.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        articleForm.setTitle(articleResponseDto.getTitle());
        articleForm.setContent(articleResponseDto.getContent());
        return "article/articleForm";
    }

//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String updateArticle(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "article/articleForm";
        }
        Article article = articleService.getArticle(id);
//        if (!articleResponseDto.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        articleService.updateArticle(article, articleForm.getTitle(), articleForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteArticle(Principal principal, @PathVariable("id") Long id) {
        Article article = articleService.getArticle(id);
//        if (!article.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
//        }
        articleService.deleteArticle(article);
        return "redirect:/article/list";
    }
}
