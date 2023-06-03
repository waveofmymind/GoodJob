package com.goodjob.domain.job.service;

import com.goodjob.domain.job.dto.JobResponseDto;
import com.goodjob.domain.job.entity.JobStatistic;
import com.goodjob.domain.job.repository.JobStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobStatisticService {
    private final JobStatisticRepository jobStatisticRepository;

    /**
     * create
     */
    @Transactional
    public void create(JobResponseDto jobResponseDto) {
        validateDuplicateCompany(jobResponseDto);

        JobStatistic company = JobStatistic.create(jobResponseDto);

        jobStatisticRepository.save(company);
    }

    private void validateDuplicateCompany(JobResponseDto jobResponseDto) {
        List<JobStatistic> companyList = jobStatisticRepository.findBySubjectAndUrl
                (jobResponseDto.getSubject(), jobResponseDto.getUrl());

        if (!companyList.isEmpty()) {
            throw new IllegalStateException("중복 입력");
        }
    }


    public List<JobResponseDto> getFilterDto(List<JobResponseDto> mainDto, List<JobResponseDto> filterDto) {
        return mainDto.stream().filter(md -> filterDto.stream().noneMatch(
                fd -> md.getSubject().equals(fd.getSubject()) &&
                        md.getCompany().equals(fd.getCompany()) &&
                        md.getCareer() == fd.getCareer())).toList();
    }



    public Page<JobStatistic> getList(String sectorCode, String career, int page){
        int sectorNum = Integer.parseInt(sectorCode);
        int careerCode = Integer.parseInt(career);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("startDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return jobStatisticRepository.findByCareerAndSectorCode(careerCode, sectorNum, pageable);
    }

//    public RsData<List<JobStatistic>> getList(JobRequestForm requestForm) {
//        if (requestForm.getCareer().isEmpty() && requestForm.getSector().isEmpty()) {
//            return getList(requestForm.getSector(), requestForm.getSector(), page);
//        } else if (requestForm.getCareer().isEmpty()) {
//            List<JobStatistic> bySectorCode = jobStatisticRepository.findBySectorCode(Integer.parseInt(requestForm.getSector()));
//            return RsData.of("S-1", "직무만 리턴", bySectorCode);
//        }
//        return RsData.of("S-1", "전부리턴",
//                jobStatisticRepository.findByCareerAndSectorCode(Integer.parseInt(requestForm.getCareer())
//                        , Integer.parseInt(requestForm.getSector())));
//    }
}




