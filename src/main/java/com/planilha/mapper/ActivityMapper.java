package com.planilha.mapper;

import com.planilha.model.ActivityVO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ActivityMapper {

    public static List<ActivityVO> map(String body) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        List<ActivityVO> activityList = new ArrayList<>();

        Document document = Jsoup.parse(body);
        Elements elements = document.getElementsByClass("texto_ativ");

        for (int i = 0; i < elements.size(); i += 12) {
            try {
                ActivityVO activityVO = new ActivityVO();
                String data = elements.get(i + 9).text().replace(" ", "");
                String hora = elements.get(i + 10).text().replace(" ", "");

                activityVO.setBegin(format.parse(data.concat(" ").concat(hora)));
                activityVO.setQuantity(Double.parseDouble(elements.get(i + 11).text().replace(" ", "").replace(",", ".")));

                activityList.add(activityVO);
            } catch (ParseException ex) {
                log.error("Error parsing date", ex);
            }
        }

        return activityList;
    }


}
