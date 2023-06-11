package com.goodjob.domain.article.entity;

import com.goodjob.domain.BaseEntity;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.hashTag.entity.HashTag;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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



    // TODO: 의존관계
}
