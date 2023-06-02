package com.goodjob.domain.article.controller;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.controller.CommentController;
import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.subComment.controller.SubCommentController;
import com.goodjob.domain.subComment.dto.request.SubCommentRequestDto;
import com.goodjob.global.base.rq.Rq;
import com.goodjob.global.base.rsData.RsData;
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
    private final Rq rq;
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
    public String detailArticle (Model model, @PathVariable("id") Long id, CommentRequestDto commentRequestDto, SubCommentRequestDto subCommentRequestDto) {
        RsData<Article> articleRsData = articleService.getArticle(id);
        if(articleRsData.isFail()) {
            return rq.historyBack(articleRsData);
        }
        ArticleResponseDto articleResponseDto = articleService.increaseViewCount(articleRsData.getData());
        model.addAttribute("article", articleResponseDto);
        return "article/detailArticle";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createArticle(ArticleRequestDto articleRequestDto ) {
        return "article/articleForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createArticle(@Valid ArticleRequestDto articleRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "article/articleForm";
        }
        articleService.createArticle(rq.getMember(), articleRequestDto);
        long id = articleService.getCreatedArticleId();
        return "redirect:/article/detail/%s".formatted(id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{id}")
    public String updateArticle(ArticleRequestDto articleRequestDto, @PathVariable("id") Long id, Principal principal) {
        RsData<ArticleResponseDto> articleResponseDtoRsData = articleService.getArticleResponseDto(id);
//        if(!article.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        articleRequestDto.setTitle(articleResponseDtoRsData.getData().getTitle());
        articleRequestDto.setContent(articleResponseDtoRsData.getData().getContent());
        return "article/articleForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String updateArticle(@Valid ArticleRequestDto articleRequestDto, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "article/articleForm";
        }
        RsData<Article> articleRsData = articleService.getArticle(id);
//        if (!articleResponseDto.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        articleService.updateArticle(articleRsData.getData(), articleRequestDto);
        return String.format("redirect:/article/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteArticle(Principal principal, @PathVariable("id") Long id) {
        RsData<Article> articleRsData = articleService.getArticle(id);
//        if (!article.getMember().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
//        }
        articleService.deleteArticle(articleRsData.getData());
        return "redirect:/article/list";
    }
}
