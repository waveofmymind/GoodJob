package com.goodjob.core.domain.mypage;

import com.goodjob.article.domain.article.entity.Article;
import com.goodjob.article.domain.comment.entity.Comment;
import com.goodjob.resume.dto.response.ResponsePredictionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberContentDto {

    private Long memberId;
    private List<Article> articles;
    private List<Comment> comments;
    private List<ResponsePredictionDto> predictions;

    public MemberContentDto(Long memberId, List<Article> articles, List<Comment> comments, List<ResponsePredictionDto> predictions) {
        this.memberId = memberId;
        this.articles = articles;
        this.comments = comments;
        this.predictions = predictions;
    }
}
