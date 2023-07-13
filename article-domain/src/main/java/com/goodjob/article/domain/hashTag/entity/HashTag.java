package com.goodjob.article.domain.hashTag.entity;



import com.goodjob.article.domain.article.entity.Article;
import com.goodjob.article.domain.keyword.entity.Keyword;
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
public class HashTag {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Keyword keyword;
}
