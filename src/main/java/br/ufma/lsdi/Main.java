package br.ufma.lsdi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Paper> papers = ReaderFile.readerXlsParsifal("files/articles.xls");
        papers.forEach(paper -> System.out.println(paper));

        Crawler crawler = new Crawler();
        papers = crawler.searchCitations(papers, 4000);
    }
}
