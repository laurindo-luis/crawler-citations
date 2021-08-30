package br.ufma.lsdi;

import br.ufma.lsdi.authentication.Credential;
import br.ufma.lsdi.authentication.LoginDigitalLibrary;
import br.ufma.lsdi.authentication.ScopusLoginDigitalLibrary;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    private final WebDriver webDriver;

    private final String XPathCitationsScienceDirect = "//*[@id=\"mathjax-container\"]/div[2]/div[2]/aside";
    private final String XPathCitationsIEEEXplorer = "//*[@id=\"LayoutWrapper\"]/div/div/div/div[3]/div/" +
            "xpl-root/div/xpl-document-details/div/div[1]/section[2]/div/xpl-document-header/section/div[2]/" +
            "div/div/div[2]/div[2]/div[1]/div[1]";
    private final String XPathCitationsACM = "//*[@id=\"pb-page-content\"]/div/main/div[2]/article/div[1]/div[2]" +
            "/div/div[6]/div/div[1]/div/ul/li[1]/span/span[1]";
    private final String XPathCitationsScopus = "//*[@id=\"recordPageBoxes\"]/div/div[1]/h3";

    private LoginDigitalLibrary loginDigitalLibraryScopus;

    Crawler() {
        System.setProperty("webdriver.chrome.driver",
			new File("assets/chromedriver").getAbsolutePath()
		);

        this.webDriver = new ChromeDriver(
                new ChromeOptions().setHeadless(false)
        );

        this.loginDigitalLibraryScopus = new ScopusLoginDigitalLibrary();
    }

    public List<Paper> searchCitations(List<Paper> papers, long delay) {
        List<Paper> listPapers = new ArrayList<>();
        WriteFileCSV writeFileCSV = new WriteFileCSV("articles_citations.csv");
        writeFileCSV.setColumnLabels("Doi", "Title", "Digital Library", "Year", "Number of Citations", "Url");

        papers.forEach(paper -> {
            if(!paper.getDoi().isEmpty() || !paper.getUrl().isEmpty()) {

                if(paper.getSource().equals("Scopus") && !paper.getUrl().isEmpty()) {
                    if(!loginDigitalLibraryScopus.isStatusLogin()) {
                        Credential credential = ReaderFile.readCredentias("assets/credentials/credential_scopus.txt");
                        loginDigitalLibraryScopus.authentication(
                                webDriver, credential.getUser(), credential.getPassword()
                        );
                    }
                    webDriver.get(paper.getUrl());
                } else if(!paper.getDoi().isEmpty()) {
                    webDriver.get(paper.getDoi());
                } else if(!paper.getUrl().isEmpty()) {
                    webDriver.get(paper.getUrl());
                }
                System.out.println("> Searching citation paper "+paper.getTitle());
                String urlBrowser = webDriver.getCurrentUrl();
                Utils.sleep(delay);

                String numberOfCitations = "0";
                if(urlBrowser.contains("www.scopus.com")) {
                    List<WebElement> elements = webDriver.findElements(By.xpath(XPathCitationsScopus));
                    if(elements.size() > 0) {
                        String textCitations = elements.get(0).getText();
                        numberOfCitations = textCitations.replaceAll("[^0-9]+", "");
                    }
                } else if(urlBrowser.contains("dl.acm.org")) {
                    List<WebElement> elements = webDriver.findElements(By.xpath(XPathCitationsACM));
                    if(elements.size() > 0) {
                        numberOfCitations = elements.get(0).getText();
                    }
                } else if(urlBrowser.contains("ieeexplore.ieee.org")) {
                    List<WebElement> elements = webDriver.findElements(By.xpath(XPathCitationsIEEEXplorer));
                    if(elements.size() > 0) {
                        String textCitations = elements.get(0).getText();
                        if(textCitations.contains("Citation") && textCitations.contains("Paper"))
                            numberOfCitations = textCitations.split("\n")[0];
                    }
                } else if(urlBrowser.contains("www.sciencedirect.com")) {
                    List<WebElement> elements = webDriver.findElements(By.xpath(XPathCitationsScienceDirect));
                    if(elements.size() > 0) {
                        String textCitations = elements.get(0).getText();
                        Pattern pattern = Pattern.compile("Citing articles \\([0-9]+\\)");
                        Matcher matcher = pattern.matcher(textCitations);
                        String resultMatecher = "";
                        while (matcher.find()) {
                            resultMatecher = matcher.group();
                        }
                        numberOfCitations = resultMatecher.replaceAll("[^0-9]+", "");
                    }
                }

                if(numberOfCitations.trim().isEmpty())
                    numberOfCitations = "0";

                Paper paperResponse = new Paper.Builder()
                        .setDoi(paper.getDoi())
                        .setTitle(paper.getTitle())
                        .setYear(paper.getYear())
                        .setSource(paper.getSource())
                        .setUrl(paper.getUrl())
                        .setNumberOfCitation(Integer.valueOf(numberOfCitations))
                        .build();

                System.out.println(paperResponse.getNumberOfCitations()+" citations");
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
