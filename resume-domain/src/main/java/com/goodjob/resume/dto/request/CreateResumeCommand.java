package com.goodjob.resume.dto.request;

import com.goodjob.resume.domain.resume.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CreateResumeCommand {

    private Long memberId;
    private String job;
    private String career;
    private List<ResumeRequest> resumeRequests;

    public Resume toEntity() {
        return Resume.builder()
                .memberId(memberId)
                .job(job)
                .career(career)
                .resumeInfoList(resumeRequests.stream().map(ResumeRequest::toEntity).toList())
                .build();
    }
}
