package com.planilha.mapper;

import com.planilha.model.ActivityListVO;
import com.planilha.model.ActivityVO;
import com.planilha.model.RowVO;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class RowMapper {

    private static final int WORKLOAD = 8;

    public static RowVO map(ActivityListVO activityList, int index) {

        if (activityList == null || activityList.getActivityList().isEmpty())
            return null;

        Collections.sort(activityList.getActivityList(), Comparator.comparing(ActivityVO::getBegin));

        RowVO row = new RowVO();
        row.setIndex(index);

        boolean[] hours = new boolean[48];

        activityList.getActivityList().forEach(activityVO -> {

            int hour = activityVO.getBegin().getHours();
            int minute = activityVO.getBegin().getMinutes();
            int idc = hour * 2;

            if (minute >= 30)
                idc++;

            int positions = (int) (activityVO.getQuantity() / 0.5d);

            for (int i = 0; i < positions; i++) {
                hours[idc + i] = true;
            }

        });

        boolean begin = false,
                beginLunch = false,
                endLunch = false,
                end = false,
                beginExtra = false,
                endExtra = false;

        double totalHours = 0d;

        Date baseDate = activityList.getDate();

        // iterate over hours array applying rules
        for (int i = 0; i < hours.length; i++) {

            // begin
            if (hours[i] && !begin) {
                begin = true;
                row.setBegin(sumHoursToDateTime(baseDate, (i / 2d)));
                continue;
            }

            // beginLunch
            if (!hours[i] && !beginLunch && begin) {
                beginLunch = true;
                row.setBeginLunch(sumHoursToDateTime(baseDate, (i / 2d)));
                continue;
            }

            // endLunch
            if (hours[i] && beginLunch && begin && !endLunch) {
                endLunch = true;
                row.setEndLunch(sumHoursToDateTime(baseDate, (i / 2d)));
                continue;
            }

            // end
            if ((!hours[i] || totalHours == WORKLOAD) && begin && beginLunch && endLunch && !end) {
                end = true;
                row.setEnd(sumHoursToDateTime(baseDate, (i / 2d)));
                continue;
            }

            // beginExtra
            if (hours[i] && totalHours == WORKLOAD && end) {
                beginExtra = true;
                row.setBeginExtra(sumHoursToDateTime(baseDate, (i / 2d)));
                continue;
            }

            // endExtra
            if (!hours[i] && beginExtra && !endExtra) {
                endExtra = true;
                row.setEndExtra(sumHoursToDateTime(baseDate, (i / 2d)));
            }


            // each iteration is equal 0.5 (30 minutes)
            if (hours[i])
                totalHours += 0.5d;

        }


        return row;
    }

    private static Date sumHoursToDateTime(Date date, double hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.clear(Calendar.HOUR);
        cal.clear(Calendar.MINUTE);

        cal.add(Calendar.HOUR_OF_DAY, (int) hours);

        if ((hours - (int) hours) == 0.5d) {
            cal.add(Calendar.MINUTE, 30);
        }

        return cal.getTime();
    }
}
