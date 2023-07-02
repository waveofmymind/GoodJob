package com.goodjob.core.domain.mentoring.entity;

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
public class Mentoring extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @Setter
    private String title;

    @Setter
    @Column(columnDefinition = "text")
    private String content;

    @Setter
    private String job;
    @Setter
    private String career;
    @Setter
    private String currentJob;
    @Setter
    private String preferredTime;
}
