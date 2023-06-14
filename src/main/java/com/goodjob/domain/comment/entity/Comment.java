package com.goodjob.domain.comment.entity;

import com.goodjob.domain.BaseEntity;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.entity.SubComment;
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
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Article article;

    @Setter
    @Column(columnDefinition = "text")
    private String content;

    @OneToMany(mappedBy = "comment", cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @Builder.Default
    private List<Likes> likesList = new ArrayList<>();

    @Setter
    private boolean isDeleted;

    @OneToMany(mappedBy = "comment", cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @Builder.Default
    private List<SubComment> subCommentList = new ArrayList<>();

}
