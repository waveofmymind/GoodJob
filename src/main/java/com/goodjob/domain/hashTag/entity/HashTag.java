package com.goodjob.domain.hashTag.entity;


import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.keyword.entity.Keyword;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class HashTag {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Keyword keyword;
}
