package com.goodjob.batch.job.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestForm {
    @NotEmpty
    private String sector;

    @NotEmpty
    private String career;
}
