package com.goodjob.core.domain.article.entity;


import com.goodjob.core.domain.BaseEntity;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.hashTag.entity.HashTag;
import com.goodjob.core.domain.likes.entity.Likes;
import com.goodjob.core.domain.member.entity.Member;
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
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    //ResponseDto 매핑 과정에서 오류가 발생해서 즉시 로딩으로 변경
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Comment> commentList;

    @Setter
    private String title;

    @Setter
    @Column(columnDefinition = "text")
    private String content;

    @Setter
    private Long viewCount;

    @Setter
    private boolean isDeleted;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @Builder.Default
    private List<Likes> likesList = new ArrayList<>();

    @Setter
    private Long commentsCount;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.ALL})
    @Builder.Default
    private List<HashTag> hashTagList = new ArrayList<>();

    @Setter
    private int category;
}
