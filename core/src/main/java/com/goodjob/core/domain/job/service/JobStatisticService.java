package com.goodjob.core.domain.job.service;



import com.goodjob.core.domain.job.dto.JobResponseDto;
import com.goodjob.core.domain.job.entity.JobStatistic;
import com.goodjob.core.domain.job.repository.JobStatisticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobStatisticService {
    private final JobStatisticRepository jobStatisticRepository;

    /**
     * create
     */
    @Transactional
    public void create(JobResponseDto jobResponseDto) {

        try {
            validateDuplicateCompany(jobResponseDto);
            JobStatistic company = JobStatistic.create(jobResponseDto);

            jobStatisticRepository.save(company);
        } catch (IllegalStateException | DataIntegrityViolationException e) {
            log.info("중복데이터");
        }


    }

    private void validateDuplicateCompany(JobResponseDto jobResponseDto) {
        List<JobStatistic> companyList = jobStatisticRepository.findByUrl(jobResponseDto.getUrl());

        if (!companyList.isEmpty()) {
            throw new IllegalStateException("중복 입력");
        }
    }


    // update
    public List<JobResponseDto> getFilterDto(List<JobResponseDto> mainDto, List<JobResponseDto> filterDto) {
        return mainDto.stream().filter(md -> filterDto.stream().noneMatch(
                fd -> md.getSubject().equals(fd.getSubject()) &&
                        md.getCompany().equals(fd.getCompany()) &&
                        md.getCareer() == fd.getCareer())).toList();
    }

    public List<JobResponseDto> sameDtoFilter(List<JobResponseDto> firstDto, List<JobResponseDto> secondDto) {
        List<JobResponseDto> returnDto = new ArrayList<>();
        for (int i = 0; i < firstDto.size(); i++) {
            int error = 0;
            for (int j = i + 1; j < firstDto.size(); j++) {
                JobResponseDto first = firstDto.get(i);
                JobResponseDto second = secondDto.get(j);
                if (first.getSubject().equals(second.getSubject()) && first.getCareer() == second.getCareer() && first.getCompany().equals(second.getCompany())) {
                    error++;
                }
            }
            if (error == 0) {
                returnDto.add(firstDto.get(i));
            }
        }
        return returnDto;
    }



    public Page<JobStatistic> getList(String sectorCode, String career, int page){
        int sectorNum = Integer.parseInt(sectorCode);
        int careerCode = Integer.parseInt(career);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("startDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        if (careerCode == -1) {
            return jobStatisticRepository.findBySectorCode(sectorNum, pageable);
        }
        return jobStatisticRepository.findByCareerAndSectorCode(careerCode, sectorNum, pageable);
    }



    @Transactional
    public void delete(JobStatistic jobStatistic) {
        jobStatisticRepository.delete(jobStatistic);
    }



    public List<JobStatistic> getAll() {
        return jobStatisticRepository.findAll();
    }


    // upsert TODO:TEST 후 변경
    @Transactional
    @Async
    public void upsert(JobResponseDto dto) {
        jobStatisticRepository.upsert(dto);
    }

}




