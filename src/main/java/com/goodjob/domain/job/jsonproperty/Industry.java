package com.goodjob.domain.job.jsonproperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Industry {
    @JsonProperty("name")
    private String name;
}
