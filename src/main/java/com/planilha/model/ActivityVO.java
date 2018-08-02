package com.planilha.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ActivityVO {
    private Date begin;
    private double quantity;
}
