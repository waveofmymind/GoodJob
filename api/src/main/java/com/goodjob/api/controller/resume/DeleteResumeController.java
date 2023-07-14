package com.goodjob.api.controller.resume;

import com.goodjob.resume.application.outs.DeletePredictionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class DeleteResumeController {

    private final DeletePredictionPort deletePredictionPort;

    @DeleteMapping("/{id}")
    public void deletePrediction(@PathVariable Long id) {
        deletePredictionPort.deletePrediction(id);
    }
}
