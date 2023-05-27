package com.goodjob.domain.job.api.jsonproperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Company {
    @JsonProperty("detail")
    private CompanyDetail detail;
}