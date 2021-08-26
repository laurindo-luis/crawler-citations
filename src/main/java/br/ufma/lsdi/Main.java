package br.ufma.lsdi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ReaderFile readerFile = new ReaderFile();
        List<Paper> papers = readerFile.reader("articles_2.xls");

        papers.forEach(paper -> {
            System.out.println(String.format("DOI -> %s - TITLE -> %s - YEAR -> %s",
                    paper.getDoi(), paper.getTitle(), paper.getYear()));
        });

        Crawler crawler = new Crawler();
        crawler.searchCitations(papers);
    }
}
