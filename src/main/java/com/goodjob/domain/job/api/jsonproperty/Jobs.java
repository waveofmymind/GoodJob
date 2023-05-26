package com.goodjob.domain.job.api.jsonproperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Jobs {
    @JsonProperty("job")
    private List<Job> job;
}
