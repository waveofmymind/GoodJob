package com.goodjob.article.domain.article.entity;


import com.goodjob.article.domain.file.entity.File;
import com.goodjob.article.domain.likes.entity.Likes;
import com.goodjob.common.BaseEntity;
import com.goodjob.article.domain.comment.entity.Comment;
import com.goodjob.article.domain.hashTag.entity.HashTag;
import com.goodjob.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
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
@Where(clause = "is_deleted = false")
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    //ResponseDto 매핑 과정에서 오류가 발생해서 즉시 로딩으로 변경
    @OneToMany(mappedBy = "article", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "article", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Likes> likesList = new ArrayList<>();

    @Setter
    private Long commentsCount;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<HashTag> hashTagList = new ArrayList<>();

    @Setter
    private int category;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<File> fileList;

    public Long updateViewCount() {
        this.viewCount += 1;
        return this.viewCount;
    }
}
