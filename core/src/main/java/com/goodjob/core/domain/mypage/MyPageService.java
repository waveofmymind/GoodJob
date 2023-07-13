package com.goodjob.core.domain.mypage;

import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.article.service.ArticleService;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.comment.service.CommentService;
import com.goodjob.resume.dto.response.ResponsePredictionDto;
import com.goodjob.resume.facade.PredictionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ArticleService articleService;
    private final CommentService commentService;
    private final PredictionFacade predictionFacade;

    public MemberContentDto getMemberContent(Long id) {
        List<Article> articles = articleService.findAllByMemberId(id);
        List<Comment> comments = commentService.findAllByMemberId(id);
        List<ResponsePredictionDto> predictions = predictionFacade.getPredictions(id);

        return new MemberContentDto(id, articles, comments, predictions);
    }
}
