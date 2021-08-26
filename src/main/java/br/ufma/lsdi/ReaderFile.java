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

    public static List<Paper> readerXlsParsifal(String path) {
        List<Paper> papers = new ArrayList<>();

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(path));

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
                    String source = isNull(row.getCell(5)) ? "" : row.getCell(5).getStringCellValue();
                    String doi = isNull(row.getCell(10)) ? "" : row.getCell(10).getStringCellValue();
                    String url = isNull(row.getCell(11)) ? "" : row.getCell(11).getStringCellValue();
                    String status = isNull(row.getCell(24)) ? "" : row.getCell(24).getStringCellValue();

                    if(title.isEmpty() && year.equals("0") && doi.isEmpty() && status.isEmpty())
                        break;

                    if(!status.equals("Duplicated")) {
                        doi = doiFormat(doi);
                        Paper paper = new Paper.Builder()
                                .setDoi(doi)
                                .setTitle(title)
                                .setUrl(url)
                                .setSource(source)
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

    private void readCredentias() {

    }

    private static String doiFormat(String doi) {
        if (!doi.isEmpty())
            if (!doi.startsWith("https://doi.org/"))
                return "https://doi.org/".concat(doi);
            else
                return doi;
        return "";
    }
}
