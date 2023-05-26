package com.goodjob.domain.job.api.jsonproperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {
    @JsonProperty("title")
    private String title;

    @JsonProperty("industry")
    private Industry industry;

    @JsonProperty("experience-level")
    private ExperienceLevel experienceLevel;
}
