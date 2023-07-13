package com.goodjob.article.domain.article.service;


import com.goodjob.article.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.article.domain.article.mapper.ArticleMapper;
import com.goodjob.article.domain.file.entity.File;
import com.goodjob.article.domain.file.service.FileService;
import com.goodjob.article.domain.subComment.entity.SubComment;
import com.goodjob.common.rsData.RsData;
import com.goodjob.article.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.article.domain.article.entity.Article;
import com.goodjob.article.domain.article.repository.ArticleRepository;
import com.goodjob.article.domain.comment.entity.Comment;
import com.goodjob.article.domain.hashTag.service.HashTagService;
import com.goodjob.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final HashTagService hashTagService;
    private final FileService fileService;


    @Transactional
    public Page<ArticleResponseDto> findByCategory(int page, int id, int sortCode, String category, String query) {
        Pageable pageable = PageRequest.of(page, 12);

        List<Article> articles = articleRepository.findQslBySortCode(id, sortCode, category, query);

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
    }

    @Transactional
    public RsData getArticleResponseDto(Long id) {
        RsData<Article> articleRsData = getArticle(id);

        if(articleRsData.isFail()) {
            return articleRsData;
        }

        Article article = articleRsData.getData();
        ArticleResponseDto articleResponseDto = increaseViewCount(article);

        return RsData.of("S-1", "게시글에 대한 정보를 가져옵니다.", articleResponseDto);
    }

    private ArticleResponseDto increaseViewCount(Article article) {
        Long viewCount = article.updateViewCount();
        countCommentsAndSubComments(article);
        Map<String, File> fileMap = getFileMap(article);
        ArticleResponseDto articleResponseDto = articleMapper.toDto(article);
        articleResponseDto.getExtra().put("fileMap", fileMap);

        return articleResponseDto;
    }

    private Map<String, File> getFileMap(Article article) {
        List<File> files = article.getFileList();

        for(File file : files) {
            file.getFileName();
        }

        return files
                .stream()
                .collect(Collectors.toMap(
                        file -> "file__" + file.getFileNo(),
                        file -> file
                ));
    }


    @Transactional
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
                .category(articleRequestDto.getCategory())
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

        article.setTitle(articleRequestDto.getTitle());
        article.setContent(articleRequestDto.getContent());
        article.setCategory(articleRequestDto.getCategory());
        hashTagService.applyHashTags(article, articleRequestDto.getHashTagStr());

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

        return RsData.of("S-1", "게시글이 삭제되었습니다.", article);
    }

    public List<Article> findAllByMemberId(Long memberId) {
        return articleRepository.findAllByMemberIdOrderByCreatedDateDesc(memberId);
    }
}
