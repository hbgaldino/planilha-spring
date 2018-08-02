package com.planilha.controller;

import com.planilha.model.UploadRequest;
import com.planilha.service.PlanilhaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class UploadController {

    @Autowired
    public PlanilhaService planilhaService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity upload(UploadRequest request) throws IOException {

        planilhaService.processWorkbook(request);

        return ResponseEntity.ok(null);

//        return ResponseEntity.ok()
//                .contentLength(request.getFile().getSize())
//                .contentType(MediaType.parseMediaType(request.getFile().getContentType()))
//                .body(new ByteArrayResource(request.getFile().getBytes()));
    }
}
