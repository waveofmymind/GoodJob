package com.goodjob.api.feign;


import com.goodjob.api.controller.resume.FindPredictionFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "goodjob-resume", url = "http://localhost:8090")
public interface RegisterPredictionFeign {

    @RequestMapping(method = RequestMethod.GET, value = "/predictions/{predictionId}")
    FindPredictionFeignResponse getPrediction(@PathVariable("predictionId") Long predictionId);

    @RequestMapping(method = RequestMethod.GET, value = "/predictions/{memberId}/result")
    List<FindPredictionFeignResponse> getPredictions(@PathVariable("memberId") Long memberId);

    @RequestMapping(method = RequestMethod.DELETE, value = "/predictions/{predictionId}")
    void deletePrediction(@PathVariable("predictionId") Long predictionId);
}
