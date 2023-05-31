package com.goodjob.domain.job.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

@Service
public class SeleniumService {
    
    public void crawlWebsite() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_win.exe");
        } else if (os.contains("mac")) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_mac");
        } else if (os.contains("linux")) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_linux");
        }

        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://www.example.com"); // 크롤링하고자 하는 웹페이지 주소로 변경해주세요.

            WebElement element = driver.findElement(By.id("element-id")); // 웹페이지의 HTML 요소 ID로 변경해주세요.
            System.out.println("Element text: " + element.getText());
        } finally {
            driver.quit();
        }
    }
}
