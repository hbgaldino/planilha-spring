package com.planilha.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class RowVO {
    private int index;

    private Date begin;
    private Date end;
    private Date beginLunch;
    private Date endLunch;
    private Date beginExtra;
    private Date endExtra;
}
