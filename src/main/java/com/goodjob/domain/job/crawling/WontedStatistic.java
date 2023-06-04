package com.goodjob.domain.job.crawling;

import com.goodjob.domain.job.dto.JobResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.goodjob.domain.job.Constants.*;

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
        String deadLine;
        // 상시채용일 경우


        if (os.contains("win")) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_win.exe");
        } else if (os.contains("mac")) {
            Process process = Runtime.getRuntime().exec("xattr -d com.apple.quarantine drivers/chromedriver_mac");
            process.waitFor();
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_mac");
        } else if (os.contains("linux")) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_linux");
        }
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.setCapability("ignoreProtectedModeSettings", true);

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // url = base1 + jobCode + base2 + year + wontedEnd
        String wontedURL = WONTED_BASE1 + jobCode + WONTED_BASE2 + career + WONTED_END;

        driver.get(wontedURL); // 크롤링하고자 하는 웹페이지 주소로 변경해주세요.

        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (true) {
            //현재 높이 저장
            Long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");

            // 스크롤
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

            // 새로운 내용이 로드될 때까지 대기
//            Thread.sleep(2000);

            // 새로운 높이를 얻음
            Long newHeight = (Long) js.executeScript("return document.body.scrollHeight");

            // 새로운 높이가 이전 높이와 같으면 스크롤이 더는 내려가지 않은 것으로 판단
            if (newHeight.equals(lastHeight)) {
                break;
            }
        }
        int elementSize = driver.findElements(By.className("Card_className__u5rsb")).size();
        log.debug(elementSize + "");

        By byTag = By.tagName("a");
        WebElement aTag = null;

        for (int i = 0; i < elementSize; i++) {
            System.out.println("for 문시작 " + elementSize + "i : " +  i);
            WebElement webElement;
            try {
                webElement = driver.findElements(By.className("Card_className__u5rsb")).get(i);
            } catch (IndexOutOfBoundsException e) {
                break;
            }
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            JavascriptExecutor executor = (JavascriptExecutor) driver;

            while (true) { //30초 동안 무한스크롤 지속
                //executeScript: 해당 페이지에 JavaScript 명령을 보내는 거
                try {
                    Thread.sleep(500); //리소스 초과 방지
                    wait.until(ExpectedConditions.visibilityOfElementLocated(byTag));
                    executor.executeScript("arguments[0].scrollIntoView(true);", webElement.findElement(byTag)); // 스크롤 이동
                    aTag = webElement.findElement(byTag);
                    executor.executeScript("arguments[0].click();", aTag); //클릭
                    url = aTag.getAttribute("href");
                } catch (StaleElementReferenceException | InterruptedException | NoSuchElementException e) {
                    System.out.println("a태그 찾는곳 에러");
                    log.debug(e.getMessage());
                }
                if (aTag != null) {
                    break;
                }
            }

//            executor.executeScript("arguments[0].click();", aTag); //클릭

            try {
                url = aTag.getAttribute("href");
            } catch (StaleElementReferenceException e) {
                continue;
            }
            long stTime = new Date().getTime();

            System.out.println("url : "  + url);

            WebElement eleSubject = null;
            WebElement eleCompany = null;
            WebElement deadLineEle = null;
            while (new Date().getTime() < stTime + 30000) { //30초 동안 무한스크롤 지속
                //executeScript: 해당 페이지에 JavaScript 명령을 보내는 거
                try {
                    Thread.sleep(500); //리소스 초과 방지
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("JobHeader_className__HttDA")));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/div[2]/section[2]/div[1]/span[2]")
                    ));

                } catch (StaleElementReferenceException | InterruptedException | TimeoutException e) {
                    System.out.println("클릭하고 들어온곳 찾는곳 에러");
                    log.debug(e.getMessage());
                    continue;
                }
                try {
                    eleSubject = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/section[2]/h2"));
                    eleCompany = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/section[2]/div[1]/h6/a"));
                    deadLineEle = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/div[2]/section[2]/div[1]/span[2]"));
                } catch (NoSuchElementException e) {
                    System.out.println("제목, 회사, 마감일 못뽑음");
                }
                if (eleSubject != null && eleCompany != null && deadLineEle != null) {
                    subject = eleSubject.getText();
                    deadLine = deadLineEle.getText();
                    company = eleCompany.getAttribute("data-company-name");
                    log.debug("company : " + company);
                    log.debug("subject : " + subject);
                    log.debug("마감일 : " + deadLineEle.getText());


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
                    System.out.println("back 까지 진행");
                    try {
                        driver.get(wontedURL);
                        Thread.sleep(1000);
                        JobResponseDto jobResponseDto = new JobResponseDto(company, subject, url, sector, sectorCode, createDate, deadLine, career);
                        WontedStatistic.setJobDtos(jobResponseDto);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    System.out.println("back 이후 진행");
                    break;
                }
            }
        }
        System.out.println("스텝 종료");
    }

}