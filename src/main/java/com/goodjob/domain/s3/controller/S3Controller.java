package com.goodjob.domain.s3.controller;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.file.dto.request.FileRequest;
import com.goodjob.domain.file.entity.File;
import com.goodjob.domain.file.service.FileService;
import com.goodjob.domain.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {
    private final S3Service s3Service;
    private final FileService fileService;

    @GetMapping("/upload")
    public String goToUpload() {
        return "s3/upload";
    }

//    @PostMapping("/upload")
//    public String uploadFile(Article article, FileRequest fileRequest) throws IOException {
//        List<String> url = s3Service.uploadFile(article, fileRequest.getFile());
//
//        fileRequest.setUrl(url);
//        fileService.save(fileRequest);
//
//        return "redirect:/s3/list";
//    }

    @GetMapping("/list")
    public String listPage(Model model) {
        List<File> fileList =fileService.getFiles();
        model.addAttribute("fileList", fileList);
        return "s3/list";
    }
}
