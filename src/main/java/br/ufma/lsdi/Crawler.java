package br.ufma.lsdi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.List;

public class Crawler {
    private WebDriver webDriver;

    private final String URL_GOOGLE_SCHOLAR = "https://scholar.google.com.br/?hl=pt";
    private final String XPathInputSearch = "//*[@id=\"gs_hdr_tsi\"]";
    private final String XPathButton = "//*[@id=\"gs_hdr_tsb\"]/span/span[1]";
    private final String XPathCitations = "//*[@id=\"gs_res_ccl_mid\"]/div/div/div[3]";

    Crawler() {
        System.setProperty("webdriver.chrome.driver",
			new File("assets/chromedriver").getAbsolutePath()
		);

        this.webDriver = new ChromeDriver(
                new ChromeOptions().setHeadless(false)
        );
    }

    public void searchCitations(List<Paper> papers) {
        webDriver.get(URL_GOOGLE_SCHOLAR);

        WriteFileCSV writeFileCSV = new WriteFileCSV("articles_citations.csv");

        papers.forEach(paper -> {
            if(!paper.getDoi().isEmpty()) {
                System.out.println(String.format("> Search citation paper %s. DOI %s",
                        paper.getTitle(), paper.getDoi()));

                Integer numberOfCitations = 0;
                webDriver.findElement(By.xpath(XPathInputSearch)).sendKeys(paper.getDoi());
                webDriver.findElement(By.xpath(XPathButton)).click();
                String textCitations = webDriver.findElement(By.xpath(XPathCitations)).getText();

                if(textCitations.startsWith("Citado por")) {
                    numberOfCitations = Integer.valueOf(textCitations.split(" ")[2].trim());
                }
                paper.setNumberOfCitations(numberOfCitations);
                writeFileCSV.write(paper);
                webDriver.findElement(By.xpath(XPathInputSearch)).clear();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Successful search! Output: citations_articles.csv");
        writeFileCSV.close();
        webDriver.quit();
    }
}
