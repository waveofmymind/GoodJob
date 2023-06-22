package com.goodjob.api.controller.file;





import com.goodjob.core.domain.file.entity.File;
import com.goodjob.core.domain.file.service.FileService;
import com.goodjob.core.domain.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/list")
    public String listPage(Model model) {
        List<File> fileList =fileService.getFiles();
        model.addAttribute("fileList", fileList);
        return "s3/list";
    }
}
