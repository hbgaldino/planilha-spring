package com.planilha.service;

import com.planilha.connector.AccwebConnector;
import com.planilha.mapper.RowMapper;
import com.planilha.model.ActivityListVO;
import com.planilha.model.ActivityVO;
import com.planilha.model.RowVO;
import com.planilha.model.UploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class PlanilhaService {

    /**
     * Read InputStream and convert to XLSX readable workbook
     *
     * @param inputStream
     * @return XSSFWorkbook
     * @throws IOException
     */
    private XSSFWorkbook readInputStream(InputStream inputStream) throws IOException {
        return new XSSFWorkbook(inputStream);
    }

    private Map<Integer, Date> fetchSheetDates(XSSFSheet sheet) {
        Map<Integer, Date> dates = new HashMap<>();
        XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(sheet.getWorkbook());

        for (int i = 0; (null != sheet.getRow(i) && null != sheet.getRow(i).getCell(0)); i++) {
            XSSFCell cell = sheet.getRow(i).getCell(0);

            switch (cell.getCellType()) {
                case 0: // Numeric
                    dates.put(i, cell.getDateCellValue());
                    break;
                case 2: // Formula
                    double milliseconds = evaluator.evaluate(cell).getNumberValue();
                    if (milliseconds > 0) {
                        dates.put(i, HSSFDateUtil.getJavaDate(milliseconds));
                    }
                    break;
            }
        }
        return dates;
    }

    private XSSFWorkbook fillWorkbook(XSSFSheet sheet, List<RowVO> rows) {

        rows.forEach(rowVO -> {
            XSSFRow row = sheet.getRow(rowVO.getIndex());

            row.getCell(1).setCellValue(rowVO.getBegin());
            row.getCell(2).setCellValue(rowVO.getBeginLunch());
            row.getCell(3).setCellValue(rowVO.getEndLunch());
            row.getCell(4).setCellValue(rowVO.getEnd());

            if (rowVO.getBeginExtra() != null && rowVO.getEndExtra() != null) {
                row.getCell(5).setCellValue(rowVO.getBeginExtra());
                row.getCell(6).setCellValue(rowVO.getEndExtra());
            }

        });


        return sheet.getWorkbook();
    }

    public byte[] processWorkbook(UploadRequest request) throws IOException {
        log.info("started processing workbook");

        XSSFWorkbook workbook = readInputStream(request.getFile().getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(request.getMonth());

        Map<Integer, Date> dates = fetchSheetDates(sheet);

        AccwebConnector connector = new AccwebConnector();
        connector.authenticate(request.getUser(), request.getPassword());

        log.info("fetching accweb data...");
        List<RowVO> rows = new ArrayList<>();
        dates.forEach((index, date) -> {
            rows.add(RowMapper.map(connector.fetch(date), index));
        });
        log.info("finished fetching accweb data!!!");
        rows.removeIf(Objects::isNull); // remove null objects

        // fill and writing workbook
        workbook = fillWorkbook(sheet, rows);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            workbook.write(byteArrayOutputStream);
        } finally {
            byteArrayOutputStream.close();
        }

        return byteArrayOutputStream.toByteArray();
    }


}
