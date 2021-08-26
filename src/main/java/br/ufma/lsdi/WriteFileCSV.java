package br.ufma.lsdi;

import java.io.*;

public class WriteFileCSV {
    BufferedWriter bufferedWriter;

    WriteFileCSV(String pathOutput) {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(pathOutput));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setLabels(String... labels)  {
        try {
            String title = "";
            for (String label : labels) {
                title = title.concat(label.concat(";"));
            }
            int sizeString = title.length();
            title = title.substring(0, sizeString - 1).concat("\n");
            bufferedWriter.write(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(Paper paper) {
        try {
            bufferedWriter.write(String.format("%s;%s;%s;%s\n", paper.getDoi(),
                    paper.getTitle(), paper.getYear(), paper.getNumberOfCitations()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
