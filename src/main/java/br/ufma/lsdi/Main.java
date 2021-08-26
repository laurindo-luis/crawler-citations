package br.ufma.lsdi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ReaderFile readerFile = new ReaderFile();
        List<Paper> papers = readerFile.reader("articles_2.xls");

        papers.forEach(paper -> System.out.println(paper));

        Crawler crawler = new Crawler();
        papers = crawler.searchCitations(papers);

    }
}
