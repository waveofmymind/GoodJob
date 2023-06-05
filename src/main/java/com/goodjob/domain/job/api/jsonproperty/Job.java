package com.goodjob.domain.job.api.jsonproperty;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @JsonProperty("url")
    private String url;

    @JsonProperty("company")
    private Company company;

    @JsonProperty("position")
    private Position position;

    @JsonProperty("posting-timestamp")
    private String postingTimestamp;


//    @JsonProperty("expiration-timestamp")
    @JsonProperty("expiration-date")
    private String expirationDate;
}
