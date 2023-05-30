package com.goodjob.domain.job.service;

import com.goodjob.domain.job.api.SaraminApiManager;
import com.goodjob.domain.job.dto.JobResponseDto;
import com.goodjob.domain.job.entity.Company;
import com.goodjob.domain.job.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {
    private final CompanyRepository companyRepository;

    /**
     * create
     */
    @Transactional
    public void create(JobResponseDto jobResponseDto) {
        validateDuplicateCompany(jobResponseDto);

        Company company = Company.create(jobResponseDto);

        companyRepository.save(company);
    }

    private void validateDuplicateCompany(JobResponseDto jobResponseDto) {
        List<Company> companyList = companyRepository.findBySubjectAndUrl
                (jobResponseDto.getSubject(), jobResponseDto.getUrl());

        if (!companyList.isEmpty()) {
            throw new IllegalStateException("중복 입력");
        }
    }


    public List<JobResponseDto> getFilterDto(List<JobResponseDto> mainDto, List<JobResponseDto> filterDto) {
        return mainDto.stream().filter(md -> filterDto.stream().noneMatch(
                fd -> md.getSubject().equals(fd.getSubject()) &&
                        md.getCreateDate().equals(fd.getCreateDate()) &&
                        md.getDeadLine().equals(fd.getDeadLine()) &&
                        md.getCompany().equals(fd.getCompany()) &&
                        md.getCareer() == fd.getCareer())).toList();
    }




}
