package com.goodjob.domain.article.service;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.mapper.ArticleMapper;
import com.goodjob.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;


    public Page<ArticleResponseDto> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 10);

        List<ArticleResponseDto> articles = articleRepository.findByIsDeletedFalse()
                .stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());

        return convertToPage(articles, pageable);
    }

    //TODO:페이징으로 안해도 되지 않나? 최신글 5개 가져오게 변경
    public Page<ArticleResponseDto> findTopFive() {
        Pageable pageable = PageRequest.of(0, 5);

        List<ArticleResponseDto> articles = articleRepository.findByIsDeletedFalse()
                .stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());

        return convertToPage(articles, pageable);
    }

    private Page<ArticleResponseDto> convertToPage(List<ArticleResponseDto> articles, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), articles.size());

        List<ArticleResponseDto> content = articles.subList(start, end);
        return new PageImpl<>(content, pageable, articles.size());
    }

    public ArticleResponseDto getArticleResponseDto(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        return articleMapper.toDto(article);
    }

    @Transactional
    public ArticleResponseDto increaseViewCount(Article article) {
        Long viewCount = article.getViewCount();
        article.setViewCount(viewCount + 1);
        articleRepository.save(article);
        return articleMapper.toDto(article);
    }

    public Article getArticle(Long id) {
        return articleRepository.findById(id).orElseThrow();
    }

    public void createArticle(ArticleRequestDto articleRequestDto) {

        Article article = Article
                .builder()
                .member(null)
                .commentList(null)
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .viewCount(0L)
                .isDeleted(false)
                .likesList(null)
                .build();

        articleRepository.save(article);
    }

    @Transactional
    public void updateArticle(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);

        articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Article article) {
        article.setDeleted(true);
        articleRepository.save(article);
    }

    public Long getCreatedArticleId() {
        Long id = (long) articleRepository.findAll().size();
        return id;
    }
}
