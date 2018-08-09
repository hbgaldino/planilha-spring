package com.planilha.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class UploadRequest {
    @NotNull
    private String user;
    @NotNull
    private String password;

    private Integer year;
    @NotNull
    private Integer month;
    @NotNull
    private MultipartFile file;
}
