package br.ufma.lsdi;

public class Paper {
    private String doi;
    private String title;
    private Integer year;
    private Integer numberOfCitations;

    public static class Builder {
        private Paper paper = new Paper();

        public Paper build() {
            return this.paper;
        }

        public Builder setDoi(String doi) {
            paper.setDoi(doi);
            return this;
        }

        public Builder setTitle(String title) {
            paper.setTitle(title);
            return this;
        }

        public Builder setYear(Integer year) {
            paper.setYear(year);
            return this;
        }

        public Builder setNumberOfCitation(Integer numberOfCitations) {
            paper.setNumberOfCitations(numberOfCitations);
            return this;
        }
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
