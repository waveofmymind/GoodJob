package com.goodjob.api.controller.mentoring;


import com.goodjob.core.domain.file.service.FileService;
import com.goodjob.core.domain.mentoring.dto.request.MentoringRequestDto;
import com.goodjob.core.domain.mentoring.entity.Mentoring;
import com.goodjob.core.domain.mentoring.service.MentoringService;
import com.goodjob.core.domain.s3.service.S3Service;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mentoring")
@Slf4j
public class MentoringController {
    private final Rq rq;
    private final MentoringService mentoringService;
    private final S3Service s3Service;
    private final FileService fileService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, ToListForm toListForm) {
        Page<Mentoring> paging = mentoringService.findAll(page, toListForm.category, toListForm.query);
        model.addAttribute("paging", paging);

        return "mentoring/list";
    }

    @GetMapping("/detail/{id}")
    public String detailArticle (Model model, @PathVariable("id") Long id) {
        RsData<Mentoring> mentoringRsData = mentoringService.getMentoring(id);
        if(mentoringRsData.isFail()) {
            return rq.historyBack(mentoringRsData);
        }

        Mentoring mentoring = mentoringRsData.getData();
        model.addAttribute("mentoring", mentoring);
        return "mentoring/detailMentoring";

    }

    @GetMapping("/create")
    public String createMentoring(MentoringRequestDto mentoringRequestDto ) {
        return "mentoring/createForm";

    }

    @PostMapping("/create")
    public String createMentoring(@Valid MentoringRequestDto mentoringRequestDto, BindingResult bindingResult,
                                  @RequestParam("job") String job, @RequestParam("career") String career) throws IOException {
        RsData<Mentoring> mentoringRsData = mentoringService.createMentoring(rq.getMember(), mentoringRequestDto, job, career);
        if(mentoringRsData.isFail()) {
            return rq.historyBack(mentoringRsData);
        }
        Mentoring mentoring = mentoringRsData.getData();

        return "redirect:/mentoring/detail/%s".formatted(mentoring.getId());
    }

    @Setter
    public static class ToListForm {
        private String category = "제목";
        private String query = "";

    }
}
