package uni.data;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import uni.service.News;

import java.util.logging.Logger;

public class Fetcher {
    private static Logger logger = Logger.getLogger(FetcherThread.class.getName());

    private NewsDAO newsDAO;

    public Fetcher(NewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    public void fetch() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.lnu.edu.ua/news/category/notices/");
        int addedCount = 0, alreadyExistsCount = 0;
        for (WebElement news : driver.findElements(By.className("post-title"))) {
            WebElement a = news.findElement(By.tagName("a"));
            boolean added = newsDAO.addIfNotExists(
                    new News(0, a.getAttribute("href"), a.getText()));
            if (added)
                addedCount++;
            else
                alreadyExistsCount++;
        }
        logger.info("Added " + addedCount + " news, already exists " + alreadyExistsCount + " news");
        driver.quit();
    }
}
