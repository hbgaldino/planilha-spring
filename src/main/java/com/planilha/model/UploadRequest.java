package com.planilha.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadRequest {

    private String user;
    private String password;
    private Integer year;
    private Integer month;
    private MultipartFile file;
}
