package com.goodjob.domain.article.service;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.mapper.ArticleMapper;
import com.goodjob.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;


    public List<ArticleResponseDto> findAll() {
        List<Article> articles = articleRepository.findByIsDeletedFalse();

        return articles
                .stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }

    public ArticleResponseDto getArticleResponseDto(Long id) {
        return articleMapper.toDto(articleRepository.findById(id).orElseThrow());
    }

    public Article getArticle(Long id) {
        return articleRepository.findById(id).orElseThrow();
    }

    public void create(String title, String content) {
        ArticleRequestDto articleRequestDto = new ArticleRequestDto(title, content);

        Article article = Article
                .builder()
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .member(null)
                .commentList(null)
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .likeCount(0L)
                .viewCount(0L)
                .isDeleted(false)
                .build();

        articleRepository.save(article);
    }

    @Transactional
    public void modify(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);

        articleRepository.save(article);
    }

    @Transactional
    public void delete(Article article) {
        article.setDeleted(true);
        articleRepository.save(article);
    }
}
