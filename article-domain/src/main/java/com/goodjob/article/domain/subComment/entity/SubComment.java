package com.goodjob.article.domain.subComment.entity;

import com.goodjob.article.domain.likes.entity.Likes;
import com.goodjob.common.BaseEntity;
import com.goodjob.article.domain.comment.entity.Comment;
import com.goodjob.member.entity.Member;
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
public class SubComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Comment comment;

    @Setter
    @Column(columnDefinition = "text")
    private String content;

    @OneToMany(mappedBy = "subComment", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Likes> likesList = new ArrayList<>();

    @Setter
    private boolean isDeleted;
}
