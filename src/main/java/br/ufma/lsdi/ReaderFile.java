package br.ufma.lsdi;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ReaderFile {

    public List<Paper> reader(String path) {
        List<Paper> papers = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);

            for (Row row : workbook.getSheetAt(0)) {
                Integer numberRow = row.getRowNum();
                if(!numberRow.equals(0)) {
                    String title = isNull(row.getCell(1)) ? "" : row.getCell(1).getStringCellValue();
                    String year =  isNull(row.getCell(4)) ? "" : row.getCell(4).getStringCellValue();
                    String doi = isNull(row.getCell(10)) ? "" : row.getCell(10).getStringCellValue();
                    String status = isNull(row.getCell(24)) ? "" : row.getCell(24).getStringCellValue();

                    if(title.isEmpty() && year.isEmpty() && doi.isEmpty() && status.isEmpty()) {
                        break;
                    }

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
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return papers;
    }
}
