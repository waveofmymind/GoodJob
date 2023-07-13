package com.goodjob.article.domain.file.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileRequest {
    private String title;
    private String url;
    private MultipartFile file;
}