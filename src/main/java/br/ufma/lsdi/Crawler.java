package br.ufma.lsdi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Crawler {
    private WebDriver webDriver;

    private final String XPathCitationsScienceDirect = "//*[@id=\"citing-articles-header\"]";
    private final String XPathCitationsIEEEXplorer = "//*[@id=\"LayoutWrapper\"]/div/div/div/div[3]/div/" +
            "xpl-root/div/xpl-document-details/div/div[1]/section[2]/div/xpl-document-header/section/div[2]/" +
            "div/div/div[2]/div[2]/div[1]/div[1]";
    private final String XPathCitationsACM = "//*[@id=\"pb-page-content\"]/div/main/div[2]/article/div[1]/div[2]" +
            "/div/div[6]/div/div[1]/div/ul/li[1]/span/span[1]";

    Crawler() {
        System.setProperty("webdriver.chrome.driver",
			new File("assets/chromedriver").getAbsolutePath()
		);

        this.webDriver = new ChromeDriver(
                new ChromeOptions().setHeadless(false)
        );
    }

    public List<Paper> searchCitations(List<Paper> papers) {
        List<Paper> listPapers = new ArrayList<>();
        WriteFileCSV writeFileCSV = new WriteFileCSV("articles_citations.csv");
        writeFileCSV.setLabels("Doi", "Title", "Year", "Number of Citations");

        papers.forEach(paper -> {
            if(!paper.getDoi().isEmpty()) {
                webDriver.get(paper.getDoi());
                String url = webDriver.getCurrentUrl();

                System.out.println(String.format("> Search citation paper %s. DOI %s",
                        paper.getTitle(), paper.getDoi()));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String numberOfCitations = "0";
                if(url.contains("dl.acm.org")) {
                    numberOfCitations = webDriver.findElement(By.xpath(XPathCitationsACM)).getText();
                } else if(url.contains("ieeexplore.ieee.org")) {
                    String textCitations = webDriver.findElement(By.xpath(XPathCitationsIEEEXplorer)).getText();
                    if(textCitations.contains("Citation"))
                        numberOfCitations = textCitations.split("\n")[0];
                    else
                        numberOfCitations = "0";
                } else if(url.contains("www.sciencedirect.com")) {
                    String textCitations = webDriver.findElement(By.xpath(XPathCitationsScienceDirect)).getText();
                    numberOfCitations = textCitations.replaceAll("[^0-9]+", "");
                }

                Paper paperResponse = new Paper.Builder()
                        .setDoi(paper.getDoi())
                        .setTitle(paper.getTitle())
                        .setYear(paper.getYear())
                        .setNumberOfCitation(Integer.valueOf(numberOfCitations))
                        .build();

                System.out.println(String.format("%s citations", paperResponse.getNumberOfCitations()));
                writeFileCSV.write(paperResponse);
                listPapers.add(paperResponse);
            }
        });

        System.out.println("Successful search! Output: citations_articles.csv");
        writeFileCSV.close();
        webDriver.quit();
        return listPapers;
    }
}
