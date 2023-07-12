package com.goodjob.api.controller.mentoring;


import com.goodjob.core.domain.file.service.FileService;
import com.goodjob.core.domain.mentoring.dto.request.MentoringRequestDto;
import com.goodjob.core.domain.mentoring.entity.Mentoring;
import com.goodjob.core.domain.mentoring.service.MentoringService;
import com.goodjob.core.domain.s3.service.S3Service;
import com.goodjob.common.rsData.RsData;
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

import java.io.IOException;

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
    public String detailMentoring (Model model, @PathVariable("id") Long id) {
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
    public String createMentoring(@Valid MentoringRequestDto mentoringRequestDto, BindingResult bindingResult) throws IOException {
        RsData<Mentoring> mentoringRsData = mentoringService.createMentoring(rq.getMember(), mentoringRequestDto);
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
    @GetMapping("/update/{id}")
    public String updateMentoring(Model model, @PathVariable("id") Long id) {
        RsData<Mentoring> mentoringRsData = mentoringService.getMentoring(id);

        if(mentoringRsData.isFail()) {
            return rq.historyBack(mentoringRsData);
        }

        Mentoring mentoring = mentoringRsData.getData();

        model.addAttribute("mentoring", mentoring);

        return "mentoring/modifyForm";
    }

    @PostMapping("/update/{id}")
    public String updateMentoring(@Valid MentoringRequestDto mentoringRequestDto, BindingResult bindingResult,
                                @PathVariable("id") Long id) throws IOException {

        RsData<Mentoring> mentoringRsData = mentoringService.updateMentoring(rq.getMember(), id, mentoringRequestDto);

        if(mentoringRsData.isFail()) {
            return rq.historyBack(mentoringRsData);
        }

        return rq.redirectWithMsg("/mentoring/detail/%s".formatted(id), mentoringRsData);
    }

    @GetMapping("/delete/{id}")
    public String deleteMentoring(@PathVariable("id") Long id) {
        RsData<Mentoring> mentoringRsData = mentoringService.deleteMentoring(rq.getMember(), id);

        if(mentoringRsData.isFail()) {
            return rq.historyBack(mentoringRsData);
        }


        return rq.redirectWithMsg("/mentoring/list", mentoringRsData);
    }
}
