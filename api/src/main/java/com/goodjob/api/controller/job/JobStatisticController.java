package com.goodjob.api.controller.job;

import com.goodjob.core.domain.job.dto.JobRequestForm;
import com.goodjob.core.domain.job.entity.JobStatistic;
import com.goodjob.core.domain.job.service.JobStatisticService;
import com.goodjob.core.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobstatistic")
public class JobStatisticController {
    private final JobStatisticService jobStatisticService;
    private final Rq rq;

    @GetMapping("/search")
    public String SearchFilter(JobRequestForm jobRequestForm) {
        return "jobstatistic/jobRequest";
    }

//    @GetMapping("/search/all")
//    public String SearchList(
//            @RequestParam(value = "sector") String sector, @RequestParam(value = "career", defaultValue = "all") String career,
//            @RequestParam(value = "place") String place, @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "keyword", defaultValue = "") String keyword,Model model
//    ) {
//
//        if (keyword == null) {
//            Page<JobStatistic> paging = jobStatisticService.getList(sector, career, page);
//            model.addAttribute("paging", paging);
//        }else {
////        Page<JobStatistic> paging = jobStatisticService.getList(sector, career, page);
//            Page<JobStatistic> paging = jobStatisticService.getQueryList(keyword, sector, career, place, page);
//            model.addAttribute("paging", paging);
//        }
//        return "jobstatistic/list";
//    }

    @GetMapping("/search/all")
    public String SearchList(
            @RequestParam(value = "sector") String sector, @RequestParam(value = "career", defaultValue = "all") String career,
            @RequestParam(value = "place") String place, @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,Model model
    ) {
        Page<JobStatistic> paging;
        if (keyword == null) {
            paging = jobStatisticService.getList(sector, place,career, page);
        }else {
//        Page<JobStatistic> paging = jobStatisticService.getList(sector, career, page);
            paging = jobStatisticService.getQueryList(keyword, sector, career, place, page);
        }
        model.addAttribute("paging", paging);
        return "jobstatistic/list";
    }


}


