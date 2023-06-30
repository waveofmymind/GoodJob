package com.goodjob.core.domain.article.service;


import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.core.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.article.mapper.ArticleMapper;
import com.goodjob.core.domain.article.repository.ArticleRepository;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.file.service.FileService;
import com.goodjob.core.domain.hashTag.service.HashTagService;
import com.goodjob.core.domain.subComment.entity.SubComment;
import com.goodjob.core.domain.member.entity.Member;
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
    private final HashTagService hashTagService;
    private final FileService fileService;


    public Page<ArticleResponseDto> findAll(int page, int sortCode, String category, String query) {
        Pageable pageable = PageRequest.of(page, 10);

        List<Article> articles = articleRepository.findQslBySortCode(sortCode, category, query);

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
        Optional<Article> articleOp = articleRepository.findQslById(id);

        if(articleOp.isEmpty()) {
            return RsData.of("F-1", "해당 게시글이 존재하지 않습니다.");
        }

        Article article = articleOp.get();

        if(article.isDeleted()) {
            return RsData.of("F-2", "해당 게시글은 이미 삭제되었습니다.");
        }


        return RsData.of("S-1", "게시글에 대한 정보를 가져옵니다.", article);
    }

    public RsData createArticle(Member author, ArticleRequestDto articleRequestDto) {
        if(articleRequestDto.getTitle().trim().equals("")) {
            return RsData.of("F-1", "제목을 입력해야 합니다.");
        }

        if(articleRequestDto.getContent().trim().equals("")) {
            return RsData.of("F-2", "내용을 입력해야 합니다.");
        }

        if(articleRequestDto.getTitle().trim().length() > 30) {
            return RsData.of("F-3", "제목은 30자 이내로 작성해야 합니다.");
        }

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

        hashTagService.applyHashTags(article, articleRequestDto.getHashTagStr());

        return RsData.of("S-1", "게시글을 성공적으로 생성하였습니다.", article);
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

        if(articleRequestDto.getTitle().trim().equals("")) {
            return RsData.of("F-4", "제목을 입력해야 합니다.");
        }

        if(articleRequestDto.getContent().trim().equals("")) {
            return RsData.of("F-5", "내용을 입력해야 합니다.");
        }

        if(articleRequestDto.getTitle().trim().length() > 30) {
            return RsData.of("F-6", "제목은 30자 이내로 작성해야 합니다.");
        }

        article.setTitle(articleRequestDto.getTitle());
        article.setContent(articleRequestDto.getContent());
        hashTagService.applyHashTags(article, articleRequestDto.getHashTagStr());

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

    public List<Article> findAllByNickname(String nickname) {
        return articleRepository.findQslBySortCode(1, "글쓴이", nickname);
    }
}
