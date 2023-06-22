package com.goodjob.core.domain.resume.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePromptRequest {

    private String job;
    private String career;
    private List<ResumeRequest> resumeRequests;
}
