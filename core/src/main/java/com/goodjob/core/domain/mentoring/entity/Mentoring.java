package com.goodjob.core.domain.mentoring.entity;

import com.goodjob.common.BaseEntity;
import com.goodjob.core.domain.mentoring.dto.request.MentoringRequestDto;
import com.goodjob.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    private String job;
    private String career;
    private String currentJob;
    private String preferredTime;

    private boolean isDeleted;

    public void update(MentoringRequestDto mentoringRequestDto) {
        this.title = mentoringRequestDto.getTitle();
        this.content = mentoringRequestDto.getContent();
        this.job = mentoringRequestDto.getJob();
        this.career = mentoringRequestDto.getCareer();
        this.currentJob = mentoringRequestDto.getCurrentJob();
        this.preferredTime = mentoringRequestDto.getPreferredTime();
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
