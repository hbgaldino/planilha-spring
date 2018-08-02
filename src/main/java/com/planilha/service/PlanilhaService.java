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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
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

    private XSSFWorkbook fillWorkbook(XSSFWorkbook workbook) {
        return null;
    }

    public void processWorkbook(UploadRequest request) throws IOException {
        XSSFWorkbook workbook = readInputStream(request.getFile().getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(request.getMonth());

        Map<Integer, Date> dates = fetchSheetDates(sheet);

        AccwebConnector connector = new AccwebConnector();
        connector.authenticate(request.getUser(), request.getPassword());

        List<RowVO> rows = new ArrayList<>();

        dates.forEach((index, date) -> {
            rows.add(RowMapper.map(connector.fetch(date), index));
        });
    }


}
