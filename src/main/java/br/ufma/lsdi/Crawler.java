package br.ufma.lsdi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.List;

public class Crawler {
    private WebDriver webDriver;

    private final String XPathCitations = "//*[@id=\"citing-articles-header\"]";

    Crawler() {
        System.setProperty("webdriver.chrome.driver",
			new File("assets/chromedriver").getAbsolutePath()
		);

        this.webDriver = new ChromeDriver(
                new ChromeOptions().setHeadless(false)
        );
    }

    public void searchCitations(List<Paper> papers) {
        WriteFileCSV writeFileCSV = new WriteFileCSV("articles_citations.csv");

        papers.forEach(paper -> {

            webDriver.get("https://"+paper.getDoi());
            String url = webDriver.getCurrentUrl();

            if(!paper.getDoi().isEmpty()) {
                System.out.println(String.format("> Search citation paper %s. DOI %s",
                        paper.getTitle(), paper.getDoi()));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(url.contains("www.sciencedirect.com")) {
                    String textCitations = webDriver.findElement(By.xpath(XPathCitations)).getText();
                    String numberOfCitations = textCitations.replaceAll("[^0-9]+", "");
                    System.out.println(String.format("%s citations", numberOfCitations));
                    paper.setNumberOfCitations(Integer.valueOf(numberOfCitations));
                    writeFileCSV.write(paper);
                }
            }
        });

        System.out.println("Successful search! Output: citations_articles.csv");
        writeFileCSV.close();
        webDriver.quit();
    }
}
