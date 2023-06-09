package com.goodjob.article.domain.likes.entity;

import com.goodjob.article.domain.article.entity.Article;
import com.goodjob.article.domain.comment.entity.Comment;
import com.goodjob.article.domain.subComment.entity.SubComment;
import com.goodjob.common.BaseEntity;
import com.goodjob.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Likes extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private SubComment subComment;
}
