package br.ufma.lsdi;

import java.io.*;

public class WriteFileCSV {
    BufferedWriter bufferedWriter;

    WriteFileCSV(String pathOutput) {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(pathOutput));
            bufferedWriter.write("Doi,Title,Year,Number of Citations\n");
        } catch (IOException e) {

        }
    }

    public void write(Paper paper) {
        try {
            bufferedWriter.write(String.format("%s,%s,%s,%s\n", paper.getDoi(),
                    paper.getTitle(), paper.getYear(), paper.getNumberOfCitations()));
        } catch (IOException e) {

        }
    }

    public void close() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {

        }
    }
}
