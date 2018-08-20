package com.planilha.model;

import lombok.Data;
import lombok.ToString;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

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

    static final int TOP = 3;
    static final int DOWN = -3;

    public Date getBegin() {
        return randomize(begin);
    }

    public Date getEnd() {
        return randomize(end);
    }

    public Date getBeginLunch() {
        return randomize(beginLunch);
    }

    public Date getEndLunch() {
        return randomize(endLunch);
    }

    public Date getBeginExtra() {
        return randomize(beginExtra);
    }

    public Date getEndExtra() {
        return randomize(endExtra);
    }

    private Date randomize(Date hora) {

        if(hora == null){
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(hora);
        cal.add(Calendar.MINUTE, random());
        return cal.getTime();
    }

    private int random() {
        Random random = new Random();
        return random.nextInt((TOP - DOWN) + 1) + DOWN;
    }
}
