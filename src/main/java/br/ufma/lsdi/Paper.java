package br.ufma.lsdi;

public class Paper {
    private String doi;
    private String title;
    private Integer year;
    private Integer numberOfCitations;

    public Paper(String doi, String title, Integer year) {
        this.doi = doi;
        this.title = title;
        this.year = year;
    }

    public Integer getNumberOfCitations() {
        return numberOfCitations;
    }

    public void setNumberOfCitations(Integer numberOfCitations) {
        this.numberOfCitations = numberOfCitations;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("DOI: %s | Title: %s | Year: %s", doi, title, year);
    }
}
