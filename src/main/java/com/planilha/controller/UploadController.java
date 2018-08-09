package com.planilha.controller;

import com.planilha.model.UploadRequest;
import com.planilha.service.PlanilhaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class UploadController {

    @Autowired
    public PlanilhaService planilhaService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity upload(@Valid UploadRequest request) throws IOException {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + request.getFile().getName())
                .contentType(MediaType.valueOf("application/vnd.ms-excel"))
                .body(planilhaService.processWorkbook(request));
    }
}
