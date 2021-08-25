package br.ufma.lsdi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderFile {

    public List<Paper> reader(String path) {
        List<Paper> papers = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);

            workbook.getSheetAt(0).forEach(row -> {
                Integer numberRow = row.getRowNum();
                if(!numberRow.equals(0)) {
                    String title = row.getCell(1).getStringCellValue();
                    String year = row.getCell(4).getStringCellValue();
                    String doi = row.getCell(10).getStringCellValue();
                    String status = row.getCell(24).getStringCellValue();

                    if(title.isEmpty() && year.isEmpty() && doi.isEmpty() && status.isEmpty())
                        return;

                    if(!status.equals("Duplicated")) {
                        if (!doi.isEmpty()) {
                            if (doi.contains("https://")) {
                                doi = doi.split("https://")[1];
                            } else {
                                doi = "doi.org/".concat(doi);
                            }
                        }
                        papers.add(new Paper(doi, title, Integer.valueOf(year)));
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return papers;
    }
}
