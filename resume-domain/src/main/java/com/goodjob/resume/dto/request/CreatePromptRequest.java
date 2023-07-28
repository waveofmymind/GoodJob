package com.goodjob.resume.dto.request;


import com.goodjob.resume.domain.resume.Resume;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreatePromptRequest {

    private Long memberId;
    private String job;
    private String career;
    private List<ResumeRequest> resumeRequests;

    public CreateResumeCommand toCommand() {
        return CreateResumeCommand.builder()
                .memberId(memberId)
                .job(job)
                .career(career)
                .resumeRequests(resumeRequests)
                .build();
    }
}
