package com.planilha.mapper;

import com.planilha.model.ActivityListVO;
import com.planilha.model.ActivityVO;
import com.planilha.model.RowVO;

import java.util.Collections;
import java.util.Comparator;

public class RowMapper {

    public static RowVO map(ActivityListVO activityList, int index) {

        if (activityList == null || activityList.getActivityList().isEmpty())
            return null;

        Collections.sort(activityList.getActivityList(), Comparator.comparing(ActivityVO::getBegin));

        RowVO row = new RowVO();
        row.setIndex(index);


        activityList.getActivityList().forEach(activityVO -> {

        });

        return row;
    }
}
