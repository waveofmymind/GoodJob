package com.goodjob.domain.job.crawling;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.goodjob.domain.job.Constants.*;

@Service
@Slf4j
public class SeleniumService {

    public void crawlWebsite() throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();

        String company; //회사명
        String subject; // 제목
        String url; // url
        String sector = null; // 직무 분야
        int sectorCode; // 직무 코드
        String createDate;
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

        int career = -1; // 년차 모두 보기는 -1
        int jobCode = 669; // 프론트 669  백엔드 872 웹개발자(풀스택) 873

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
            Thread.sleep(2000);

            // 새로운 높이를 얻음
            Long newHeight = (Long) js.executeScript("return document.body.scrollHeight");

            // 새로운 높이가 이전 높이와 같으면 스크롤이 더는 내려가지 않은 것으로 판단
            if (newHeight.equals(lastHeight)) {
                break;
            }
        }
        int elementSize = driver.findElements(By.className("Card_className__u5rsb")).size();
        System.out.println("총 개수 : " + elementSize);

        List<String> companyName = new ArrayList<>();
        By byTag = By.tagName("a");
        WebElement aTag= null;

        for (int i = 0; i < elementSize; i++) {
            WebElement webElement = driver.findElements(By.className("Card_className__u5rsb")).get(i);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            JavascriptExecutor executor = (JavascriptExecutor) driver;
            while (true) { //30초 동안 무한스크롤 지속
                Thread.sleep(500); //리소스 초과 방지
                //executeScript: 해당 페이지에 JavaScript 명령을 보내는 거
                try {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(byTag));
                    executor.executeScript("arguments[0].scrollIntoView(true);", webElement.findElement(byTag)); // 스크롤 이동
                } catch (StaleElementReferenceException e) {
                    log.error(e.getMessage());
                    log.debug("a태그 클릭하는곳 에러임");
                }
                aTag = webElement.findElement(byTag);
                if (aTag != null) {
                    break;
                }
            }

            executor.executeScript("arguments[0].click();", aTag); //클릭


            long stTime = new Date().getTime();

            while (new Date().getTime() < stTime + 30000) { //30초 동안 무한스크롤 지속
                Thread.sleep(500); //리소스 초과 방지
                //executeScript: 해당 페이지에 JavaScript 명령을 보내는 거
                try {
                    ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
                } catch (StaleElementReferenceException e) {
                    log.error(e.getMessage());
                    log.debug("클릭하고 들어온곳 에러임");
                }
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("JobHeader_className__HttDA")));

            // subject company
            WebElement eleSubject = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/section[2]/h2"));
            subject = eleSubject.getText();
            WebElement eleCompany = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/section[2]/div[1]/h6/a"));
            company = eleCompany.getAttribute("data-company-name");
            System.out.println("company : "+company); //회사명 출력
            System.out.println("subject : "+subject);

            // deadLine
            By locator = By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/div[2]/section[2]/div[1]/span[2]");
            WebElement deadLineEle = driver.findElement(locator);
            deadLine = deadLineEle.getText();
            System.out.println(deadLineEle.getText());



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
            driver.navigate().back();
            Thread.sleep(1000);
            System.out.println("back 이후 진행");
//            TODO: URL 값 얻어서 입력
        }




//        for (WebElement webElement : elements) {
//            Thread.sleep(1000);
//            System.out.println(webElement.getText());
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//            JavascriptExecutor executor = (JavascriptExecutor) driver;
//            while (true) { //30초 동안 무한스크롤 지속
//                Thread.sleep(500); //리소스 초과 방지
//                //executeScript: 해당 페이지에 JavaScript 명령을 보내는 거
//                try {
//                    wait.until(ExpectedConditions.visibilityOfElementLocated(byTag));
//                    executor.executeScript("arguments[0].scrollIntoView(true);", webElement.findElement(byTag)); // 스크롤 이동
//                } catch (StaleElementReferenceException e) {
//                    log.error(e.getMessage());
//                    log.debug("a태그 클릭하는곳 에러임");
//                }
//                aTag = webElement.findElement(byTag);
//                if (aTag != null) {
//                    break;
//                }
//            }
//
//            executor.executeScript("arguments[0].click();", aTag); //클릭
//
//
//            long stTime = new Date().getTime();
//
//            while (new Date().getTime() < stTime + 30000) { //30초 동안 무한스크롤 지속
//                Thread.sleep(500); //리소스 초과 방지
//                //executeScript: 해당 페이지에 JavaScript 명령을 보내는 거
//                try {
//                    ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
//                } catch (StaleElementReferenceException e) {
//                    log.error(e.getMessage());
//                    log.debug("클릭하고 들어온곳 에러임");
//                }
//            }
//
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("JobHeader_className__HttDA")));
//
//            // subject company
//            WebElement eleSubject = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/section[2]/h2"));
//            subject = eleSubject.getText();
//            WebElement eleCompany = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/section[2]/div[1]/h6/a"));
//            company = eleCompany.getAttribute("data-company-name");
//            System.out.println("company : "+company); //회사명 출력
//            System.out.println("subject : "+subject);
//
//            // deadLine
//            By locator = By.xpath("//*[@id=\"__next\"]/div[3]/div[1]/div[1]/div[1]/div[2]/section[2]/div[1]/span[2]");
//            WebElement deadLineEle = driver.findElement(locator);
//            deadLine = deadLineEle.getText();
//            System.out.println(deadLineEle.getText());
//
//
//
//            if (jobCode == 669) {
//                sectorCode = 92;
//                sector = "프론트";
//            } else if (jobCode == 872) {
//                sectorCode = 84;
//                sector = "백엔드";
//            } else if (jobCode == 873) {
//                sectorCode = 2232;
//                sector = "풀스택";
//            }
//            System.out.println("back 까지 진행");
//            driver.navigate().back();
//            Thread.sleep(1000);
//            System.out.println("back 이후 진행");
////            TODO: URL 값 얻어서 입력
//            JobResponseDto jobResponseDto = new JobResponseDto(company, subject, url입력 , sector, sectorCode, createDate, deadLine, career);
//        }

    }
}

// 원하는 클래스 이름을 가진 모든 요소를 찾습니다.
//    List<WebElement> elements = driver.findElements(By.className("your-class-name"));
//
//// 각 요소를 순회하며 클릭하고 정보를 가져옵니다.
//for (WebElement element : elements) {
//        // 요소를 클릭합니다.
//        element.click();
//
//        // 새로운 내용이 로드될 때까지 대기합니다.
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("JobWorkPlace_className__ra6rp")));
//
//        // 새로운 내용을 크롤링합니다.
//        WebElement newContent = driver.findElement(By.className("new-content-class-name"));
//        System.out.println(newContent.getText());
//
//        // 원래 페이지로 돌아가기 위해 뒤로 가기를 실행합니다.
//        // 이는 웹 사이트의 동작에 따라 필요할 수도, 필요하지 않을 수도 있습니다.
//        driver.navigate().back();
//        }