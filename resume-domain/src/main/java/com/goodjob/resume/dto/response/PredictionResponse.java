package com.goodjob.resume.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponse{
    public String question;

    public String bestAnswer;

}
