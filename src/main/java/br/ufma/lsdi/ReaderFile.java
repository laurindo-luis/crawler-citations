package br.ufma.lsdi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
                    String year = "0";
                    Cell cellYear = row.getCell(4);
                    if(nonNull(cellYear)) {
                        if(cellYear.getCellType().equals(CellType.NUMERIC))
                            year = String.valueOf((int)cellYear.getNumericCellValue());
                        else
                            year = cellYear.getStringCellValue();
                    }
                    String doi = isNull(row.getCell(10)) ? "" : row.getCell(10).getStringCellValue();
                    String status = isNull(row.getCell(24)) ? "" : row.getCell(24).getStringCellValue();

                    if(title.isEmpty() && year.equals("0") && doi.isEmpty() && status.isEmpty()) {
                        break;
                    }

                    if(!status.equals("Duplicated")) {
                        if (!doi.isEmpty()) {
                            if (!doi.startsWith("https://doi.org/")) {
                                doi = "https://doi.org/".concat(doi);
                            }
                        }

                        Paper paper = new Paper.Builder()
                                .setDoi(doi)
                                .setTitle(title)
                                .setYear(Integer.valueOf(year))
                                .build();

                        papers.add(paper);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return papers;
    }
}
