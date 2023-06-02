package com.goodjob.domain.article.service;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.mapper.ArticleMapper;
import com.goodjob.domain.article.repository.ArticleRepository;
import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.dto.response.SubCommentResponseDto;
import com.goodjob.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

        for(ArticleResponseDto articleResponseDto : articles) {
            countCommentsAndSubComments(articleResponseDto);
        }

        return convertToPage(articles, pageable);
    }

    //TODO:페이징으로 안해도 되지 않나? 최신글 5개 가져오게 변경
    public Page<ArticleResponseDto> findTopFive() {
        Pageable pageable = PageRequest.of(0, 5);

        List<ArticleResponseDto> articles = articleRepository.findByIsDeletedFalse()
                .stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());

        for(ArticleResponseDto articleResponseDto : articles) {
            countCommentsAndSubComments(articleResponseDto);
        }

        return convertToPage(articles, pageable);
    }

    private Page<ArticleResponseDto> convertToPage(List<ArticleResponseDto> articles, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), articles.size());

        List<ArticleResponseDto> content = articles.subList(start, end);
        return new PageImpl<>(content, pageable, articles.size());
    }

    private void countCommentsAndSubComments(ArticleResponseDto articleResponseDto) {
        List<CommentResponseDto> commentResponseDtos = articleResponseDto.getCommentList();
        Long sum = 0L;

        for(CommentResponseDto commentResponseDto : commentResponseDtos) {
            if (!commentResponseDto.isDeleted()) {
                sum++;
                List<SubCommentResponseDto> subCommentResponseDtos = commentResponseDto.getSubCommentList();
                for(SubCommentResponseDto subCommentResponseDto : subCommentResponseDtos) {
                    if(!subCommentResponseDto.isDeleted()) {
                        sum++;
                    }
                }
            }
        }

        articleResponseDto.setCommentsCount(sum);
    }

    public RsData<ArticleResponseDto> getArticleResponseDto(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if(article.isEmpty()) {
            return RsData.of("F-1", "해당 게시글이 존재하지 않습니다.");
        }
        return RsData.of("S-1", "게시글에 대한 정보를 나타냅니다.", articleMapper.toDto(article.get()));
    }

    @Transactional
    public ArticleResponseDto increaseViewCount(Article article) {
        Long viewCount = article.getViewCount();
        article.setViewCount(viewCount + 1);
        articleRepository.save(article);
        ArticleResponseDto articleResponseDto = articleMapper.toDto(article);
        countCommentsAndSubComments(articleResponseDto);
        return articleResponseDto;
    }

    public RsData<Article> getArticle(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if(article.isEmpty()) {
            return RsData.of("F-1", "해당 게시글이 존재하지 않습니다.");
        }
        return RsData.of("S-1", "게시글에 대한 정보를 가져옵니다.", article.get());
    }

    public void createArticle(Member author, ArticleRequestDto articleRequestDto) {

        Article article = Article
                .builder()
                .member(author)
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
    public void updateArticle(Article article, ArticleRequestDto articleRequestDto) {
        article.setTitle(articleRequestDto.getTitle());
        article.setContent(articleRequestDto.getContent());

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
