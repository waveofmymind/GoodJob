package com.goodjob.domain.article.service;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.mapper.ArticleMapper;
import com.goodjob.domain.article.repository.ArticleRepository;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.entity.SubComment;
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


    public Page<ArticleResponseDto> findAll(int page, int sortCode, String category, String query) {
        Pageable pageable = PageRequest.of(page, 10);

        List<Article> articles = articleRepository.findQslBySortCode(sortCode, category, query);

        List<ArticleResponseDto> articleResponseDtos = articles
                .stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());

        return convertToPage(articleResponseDtos, pageable);
    }

    public Page<ArticleResponseDto> findTopFive() {
        Pageable pageable = PageRequest.of(0, 5);

        List<Article> articles = articleRepository.findQslBySortCode(1, "제목", "");

        List<ArticleResponseDto> articleResponseDtos = articles
                .stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());

        return convertToPage(articleResponseDtos, pageable);
    }

    private Page<ArticleResponseDto> convertToPage(List<ArticleResponseDto> articles, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), articles.size());

        List<ArticleResponseDto> content = articles.subList(start, end);
        return new PageImpl<>(content, pageable, articles.size());
    }

    private void countCommentsAndSubComments(Article article) {
        List<Comment> commentList = article.getCommentList();
        Long sum = 0L;

        for(Comment comment : commentList) {
            if (!comment.isDeleted()) {
                sum++;
                List<SubComment> subCommentList = comment.getSubCommentList();
                for(SubComment subComment : subCommentList) {
                    if(!subComment.isDeleted()) {
                        sum++;
                    }
                }
            }
        }

        article.setCommentsCount(sum);
        articleRepository.save(article);

    }

    public RsData getArticleResponseDto(Long id) {
        RsData<Article> articleRsData = getArticle(id);

        if(articleRsData.isFail()) {
            return articleRsData;
        }

        Article article = articleRsData.getData();

        return RsData.of("S-1", "게시글에 대한 정보를 가져옵니다.", articleMapper.toDto(article));
    }

    @Transactional
    public ArticleResponseDto increaseViewCount(Article article) {
        Long viewCount = article.getViewCount();
        article.setViewCount(viewCount + 1);
        countCommentsAndSubComments(article);
        ArticleResponseDto articleResponseDto = articleMapper.toDto(article);
        return articleResponseDto;
    }

    public RsData getArticle(Long id) {
        Optional<Article> articleOp = articleRepository.findById(id);

        if(articleOp.isEmpty()) {
            return RsData.of("F-1", "해당 게시글이 존재하지 않습니다.");
        }

        Article article = articleOp.get();

        if(article.isDeleted()) {
            return RsData.of("F-2", "해당 게시글은 이미 삭제되었습니다.");
        }

        return RsData.of("S-1", "게시글에 대한 정보를 가져옵니다.", article);
    }

    public void createArticle(Member author, ArticleRequestDto articleRequestDto) {

        System.out.println(articleRequestDto.getHashTagStr());
        Article article = Article
                .builder()
                .member(author)
                .commentList(null)
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .viewCount(0L)
                .commentsCount(0L)
                .isDeleted(false)
                .likesList(null)
                .build();

        articleRepository.save(article);
    }

    @Transactional
    public RsData updateArticle(Member author, Long id, ArticleRequestDto articleRequestDto) {
        RsData<Article> articleRsData = getArticle(id);

        if(articleRsData.isFail()) {
            return articleRsData;
        }

        Article article = articleRsData.getData();

        if(article.getMember().getId() != author.getId()) {
            return RsData.of("F-3", "수정 권한이 없습니다.");
        }

        article.setTitle(articleRequestDto.getTitle());
        article.setContent(articleRequestDto.getContent());

        articleRepository.save(article);

        return RsData.of("S-1", "게시글이 수정되었습니다.", article);
    }

    @Transactional
    public RsData deleteArticle(Member author, Long id) {
        RsData<Article> articleRsData = getArticle(id);

        if(articleRsData.isFail()) {
            return articleRsData;
        }

        Article article = articleRsData.getData();

        if(article.getMember().getId() != author.getId()) {
            return RsData.of("F-3", "삭제 권한이 없습니다.");
        }

        List<Comment> commentList = article.getCommentList();

        for(Comment comment : commentList) {
            comment.setDeleted(true);

            List<SubComment> subCommentList = comment.getSubCommentList();

            for(SubComment subComment : subCommentList) {
                subComment.setDeleted(true);
            }
        }

        article.setDeleted(true);

        articleRepository.save(article);

        return RsData.of("S-1", "게시글이 삭제되었습니다.", article);
    }

    public Long getCreatedArticleId() {
        Long id = (long) articleRepository.findAll().size();
        return id;
    }

    public RsData isLoggedIn(Member member) {
        if(member == null) {
            return RsData.of("F-1", "게시글을 작성하려면 로그인을 해야 합니다.");
        }

        return RsData.of("S-1", "게시글 작성 페이지로 이동합니다.");
    }
}
