package com.goodjob.domain.article.entity;

import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Article {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne
    private Member member;

    //ResponseDto 매핑 과정에서 오류가 발생해서 즉시 로딩으로 변경
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Comment> commentList;

    @Setter
    private String title;

    @Setter
    private String content;

    private Long likeCount;

    private Long viewCount;

    @Setter
    private boolean isDeleted;

    // TODO: 의존관계
}
