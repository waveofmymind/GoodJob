package com.goodjob.domain.file.service;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.file.entity.File;
import com.goodjob.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public void save(Article article, String url, Long fileNo, String fileName) {
        File file = File
                .builder()
                .article(article)
                .fileName(fileName)
                .s3Url(url)
                .fileNo(fileNo)
                .build();

        fileRepository.save(file);
    }

    public List<File> getFiles() {
        List<File> all = fileRepository.findAll();
        return all;
    }

    public List<File> findAllByArticle(Article article) {
        return fileRepository.findAllByArticle(article);
    }

    public List<File> findAllByArticleId(Long id) {
        return fileRepository.findAllByArticleId(id);
    }

    public Map<String, File> getFileMap(Long id) {
        List<File> files = fileRepository.findAllByArticleId(id);

        return files
                .stream()
                .collect(Collectors.toMap(
                        file -> "file__" + file.getFileNo(),
                        file -> file
                ));
    }

    public void delete(File file) {
        fileRepository.delete(file);
    }

    public Optional<File> findByArticleAndFileNo(Article article, int fileNo) {
        return fileRepository.findByArticleAndFileNo(article, fileNo);
    }
}
