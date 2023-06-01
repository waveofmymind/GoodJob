package com.goodjob.domain.likes.entity;

import com.goodjob.domain.BaseEntity;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.member.entity.Member;
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
    private Member member;  //한 명의 회원이 여러 게시물 또는 댓글에 좋아요를 할 수 있다.

    @ManyToOne
    private Article article;    //한 게시글에 여러 개의 좋아요가 생길 수 있다.

    @ManyToOne
    private Comment comment;    //한 댓글에 여러 개의 좋아요가 생길 수 있다.
}
