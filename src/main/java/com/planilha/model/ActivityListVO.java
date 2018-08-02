package com.planilha.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ActivityListVO {
    private Date date;
    private List<ActivityVO> activityList;


    public double getTotal() {
        return activityList.stream().mapToDouble(i -> i.getQuantity()).sum();
    }
}
