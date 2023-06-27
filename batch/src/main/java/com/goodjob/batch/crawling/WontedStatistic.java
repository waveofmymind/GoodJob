package com.goodjob.batch.crawling;

import com.goodjob.batch.job.dto.JobResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.goodjob.batch.job.Constants.*;


@Component
@Slf4j
public class WontedStatistic {

    private final static List<JobResponseDto> jobResponseDtos = new ArrayList<>();

    public static List<JobResponseDto> getJobResponseDtos() {
        return jobResponseDtos;
    }

    public static void setJobDtos(JobResponseDto jobDtos) {
        jobResponseDtos.add(jobDtos);
    }

    public static void resetList() {
        jobResponseDtos.clear();
    }


    /**
     * @param jobCode 프론트 669, 백엔드 872, 풀스택(웹개발) 873
     * @param career  년차를 뜻함
     * @throws IOException
     * @throws InterruptedException
     */
    @Async
    public void crawlWebsite(int jobCode, int career) throws InterruptedException, IOException, WebDriverException {
        String os = System.getProperty("os.name").toLowerCase();

        String company; //회사명
        String subject; // 제목
        String url; // url
        String sector = null; // 직무 분야
        int sectorCode = 0; // 직무 코드
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String createDate = localDateTime.format(dateFormatter);


        if (os.contains("win")) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_win.exe");
        } else if (os.contains("mac")) {
            Process process = Runtime.getRuntime().exec("xattr -d com.apple.quarantine drivers/chromedriver_mac");
            process.waitFor();
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_mac");
        } else if (os.contains("linux")) {
            System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        }
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");
//        chromeOptions.setCapability("ignoreProtectedModeSettings", true); //deprecated

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // url = base1 + jobCode + base2 + year + wontedEnd
        String wontedURL = WONTED_BASE1 + jobCode + WONTED_BASE2 + career + WONTED_END;

        driver.get(wontedURL); // 크롤링하고자 하는 웹페이지 주소로 변경해주세요.

        JavascriptExecutor js = (JavascriptExecutor) driver;

        int cnt = 0;
        while (true) {
            //현재 높이 저장
            try {
                Long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");


                // 스크롤
                js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

                // 새로운 내용이 로드될 때까지 대기
                Thread.sleep(2000);
                
                // 새로운 높이를 얻음
                Long newHeight = (Long) js.executeScript("return document.body.scrollHeight");
                if (newHeight.equals(lastHeight)) {
                    break;
                }
            } catch (InterruptedException e) {
                cnt++;
                if (cnt > 100) {
                    break;
                }
            }

        }
        LocalDateTime now = LocalDateTime.now();
        if (jobCode == 669) {
            sectorCode = 92;
            sector = "프론트";
        } else if (jobCode == 872) {
            sectorCode = 84;
            sector = "백엔드";
        } else if (jobCode == 873) {
            sectorCode = 2232;
            sector = "풀스택";
        }

        List<WebElement> webElements = driver.findElements(By.className("Card_className__u5rsb"));


        for (int i = 1; i < webElements.size(); i++) {
            try {
                WebElement webElement = webElements.get(i);

                subject = webElement.findElement(By.xpath(String.format("//*[@id=\"__next\"]/div[3]/div/div/div[4]/ul/li[%d]/div/a/div/div[1]", i))).getText();// 공고제목
                company = webElement.findElement(By.xpath(String.format("//*[@id=\"__next\"]/div[3]/div/div/div[4]/ul/li[%d]/div/a/div/div[2]", i))).getText();// 회사명
                url = webElement.findElement(By.xpath(String.format("//*[@id=\"__next\"]/div[3]/div/div/div[4]/ul/li[%d]/div/a", i))).getAttribute("href");
                JobResponseDto jobResponseDto = new JobResponseDto(company, subject, url, sector, sectorCode, createDate, "상시채용", career);
                System.out.println(company);
                WontedStatistic.setJobDtos(jobResponseDto);
            } catch (StaleElementReferenceException e) {
                log.debug(e.getMessage());
            }

        }
        driver.quit();
    }
}