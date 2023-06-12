package com.goodjob.domain.resume.dto.request;

import java.util.List;

public record CreatePromptRequest(String job, String career, List<String> resumeType, List<String> content){

}