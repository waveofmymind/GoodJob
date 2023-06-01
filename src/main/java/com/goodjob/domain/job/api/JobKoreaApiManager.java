package com.goodjob.domain.job.api;


import com.goodjob.domain.job.Constants;
import com.goodjob.domain.job.api.jaxb.DataListTag;
import com.goodjob.domain.job.api.jaxb.Items;
import com.goodjob.domain.job.dto.JobResponseDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JobKoreaApiManager {
    private static JobKoreaApiManager instance = null;
    private static final List<JobResponseDto> jobResponseDtos;

    private String key;
    static {
        instance = new JobKoreaApiManager();
        jobResponseDtos = new ArrayList<>();
    }


    public static List<JobResponseDto> getJobResponseDtos() {
        return jobResponseDtos;
    }
    public static void setJobDtos(JobResponseDto jobDtos) {
        jobResponseDtos.add(jobDtos);
    }


    public static void jobKoreaStatistic()  {
        URL url = null;
        int career = 0;
        int sectorCode = 0; // 채용원하는 분야
        try {
            url = new URL(Constants.JOBKOREA); // api 발급시 url + key 입력
        } catch (MalformedURLException e) {
            log.error("JobKorea Url 잘못 됐음");
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DataListTag.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            DataListTag dataListTag = (DataListTag) unmarshaller.unmarshal(url);

            for (Items item : dataListTag.getItems()) {
                career = item.getCareer();
                LocalDateTime createDate = LocalDateTime.parse(item.getCreateData());
                LocalDateTime deadLine = LocalDateTime.parse(item.getDeadLine());

                JobResponseDto jobResponseDto = new JobResponseDto(
                        item.getCompany(), item.getSubject(), item.getUrl(),
                        item.getSector(),sectorCode, createDate, deadLine, career);

                setJobDtos(jobResponseDto);
            }
        } catch (JAXBException e) {
            log.error("Jaxb 디펜던시 수정 필요");
        }
    }

}
