package com.goodjob.domain.article.controller;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.file.dto.request.FileRequest;
import com.goodjob.domain.file.entity.File;
import com.goodjob.domain.file.service.FileService;
import com.goodjob.domain.s3.service.S3Service;
import com.goodjob.domain.subComment.dto.request.SubCommentRequestDto;
import com.goodjob.global.base.rq.Rq;
import com.goodjob.global.base.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
@Slf4j
public class ArticleController {
    private final Rq rq;
    private final ArticleService articleService;
    private final S3Service s3Service;
    private final FileService fileService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, ToListForm toListForm) {
        Page<ArticleResponseDto> paging = articleService.findAll(page, toListForm.sortCode, toListForm.category, toListForm.query);
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
        Map<String, File> fileMap = fileService.getFileMap(articleResponseDto.getId());
        articleResponseDto.getExtra().put("fileMap", fileMap);

        model.addAttribute("article", articleResponseDto);
        return "article/detailArticle";

    }

    @GetMapping("/create")
    public String createArticle(ArticleRequestDto articleRequestDto ) {
        //TODO: 찬규님꺼 pull 받고 다시 고민
//        RsData isLoggedInRsData = articleService.isLoggedIn(rq.getMember());
//        if(isLoggedInRsData.isFail()) {
//            return rq.redirectWithMsg("/member/login", isLoggedInRsData);
//        }
        return "article/createForm";

    }

    @PostMapping("/create")
    public String createArticle(@Valid ArticleRequestDto articleRequestDto, BindingResult bindingResult, MultipartRequest multipartRequest, FileRequest fileRequest) throws IOException {
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        RsData<Article> articleRsData = articleService.createArticle(rq.getMember(), articleRequestDto);
        if(articleRsData.isFail()) {
            return rq.historyBack(articleRsData);
        }
        Article article = articleRsData.getData();
        s3Service.uploadFile(article, fileMap);


        return "redirect:/article/detail/%s".formatted(article.getId());
    }


    @GetMapping("/update/{id}")
    public String updateArticle(Model model, @PathVariable("id") Long id) {
        RsData<ArticleResponseDto> articleResponseDtoRsData = articleService.getArticleResponseDto(id);

        if(articleResponseDtoRsData.isFail()) {
            return rq.historyBack(articleResponseDtoRsData);
        }

        ArticleResponseDto articleResponseDto = articleResponseDtoRsData.getData();
        Map<String, File> fileMap = fileService.getFileMap(articleResponseDto.getId());
        articleResponseDto.getExtra().put("fileMap", fileMap);

        model.addAttribute("article", articleResponseDto);

        return "article/modifyForm";
    }

    @PostMapping("/update/{id}")
    public String updateArticle(@Valid ArticleRequestDto articleRequestDto, BindingResult bindingResult,
                                 @PathVariable("id") Long id, MultipartRequest multipartRequest,
                                FileRequest fileRequest, @RequestParam Map<String, String> params) throws IOException {

        RsData<Article> articleRsData = articleService.updateArticle(rq.getMember(), id, articleRequestDto);

        if(articleRsData.isFail()) {
            return rq.historyBack(articleRsData);
        }

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        Article article = articleRsData.getData();
        for(String value : params.keySet()) {
            log.info(value);
        }
        s3Service.deleteFiles(article, params);
        s3Service.uploadFile(article, fileMap);

        return rq.redirectWithMsg("/article/detail/%s".formatted(id), articleRsData);
    }

    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id) {
        RsData<Article> articleRsData = articleService.deleteArticle(rq.getMember(), id);

        if(articleRsData.isFail()) {
            return rq.historyBack(articleRsData);
        }


        return rq.redirectWithMsg("/article/list", articleRsData);
    }

    @Setter
    public static class ToListForm {
        private int sortCode = 1;
        private String category = "제목";
        private String query = "";

    }
}
