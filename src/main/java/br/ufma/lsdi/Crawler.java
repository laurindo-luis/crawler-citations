package br.ufma.lsdi;

import br.ufma.lsdi.authentication.LoginDigitalLibrary;
import br.ufma.lsdi.authentication.ScopusLogin;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

public class Crawler {
    private final WebDriver webDriver;

    private final String XPathCitationsScienceDirect = "//*[@id=\"mathjax-container\"]/div[2]/div[2]/aside";
    private final String XPathCitationsIEEEXplorer = "//*[@id=\"LayoutWrapper\"]/div/div/div/div[3]/div/" +
            "xpl-root/div/xpl-document-details/div/div[1]/section[2]/div/xpl-document-header/section/div[2]/" +
            "div/div/div[2]/div[2]/div[1]/div[1]";
    private final String XPathCitationsACM = "//*[@id=\"pb-page-content\"]/div/main/div[2]/article/div[1]/div[2]" +
            "/div/div[6]/div/div[1]/div/ul/li[1]/span/span[1]";

    LoginDigitalLibrary loginDigitalLibrary = new ScopusLogin();

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
        writeFileCSV.setLabels("Doi", "Title", "Digital Library", "Year", "Number of Citations");

        papers.forEach(paper -> {
            if(!paper.getDoi().isEmpty() || !paper.getUrl().isEmpty()) {
                System.out.println("> Search citation paper "+paper.getTitle());

                if(paper.getSource().equals("Scopus") && !paper.getUrl().isEmpty())
                    webDriver.get(paper.getUrl());
                else
                    webDriver.get(paper.getDoi());

                String urlBrowser = webDriver.getCurrentUrl();
                Utils.sleep(3000);

                String numberOfCitations = "0";
                if(urlBrowser.contains("www.scopus.com")) {
//                    if(loginDigitalLibrary.isStatusLogin()) {
//
//                    } else {
//                        loginDigitalLibrary.authentication(webDriver, "luis.laurindo@discente.ufma.br",
//                                "rockandrollcar1996");
//                        System.out.println("Realizar Login");
//                    }
                } else if(urlBrowser.contains("dl.acm.org")) {
                    numberOfCitations = webDriver.findElement(By.xpath(XPathCitationsACM)).getText();
                } else if(urlBrowser.contains("ieeexplore.ieee.org")) {
                    String textCitations = webDriver.findElement(By.xpath(XPathCitationsIEEEXplorer)).getText();
                    if(textCitations.contains("Citation"))
                        numberOfCitations = textCitations.split("\n")[0];
                } else if(urlBrowser.contains("www.sciencedirect.com")) {
                    String textCitations = webDriver.findElement(By.xpath(XPathCitationsScienceDirect)).getText();

                    Pattern pattern = Pattern.compile("Citing articles \\([0-9]+\\)");
                    Matcher matcher = pattern.matcher(textCitations);
                    String resultMatecher = "";
                    while (matcher.find()) {
                        resultMatecher = matcher.group();
                    }

                    numberOfCitations = resultMatecher.replaceAll("[^0-9]+", "");
                }

                Paper paperResponse = new Paper.Builder()
                        .setDoi(paper.getDoi())
                        .setTitle(paper.getTitle())
                        .setYear(paper.getYear())
                        .setSource(paper.getSource())
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
