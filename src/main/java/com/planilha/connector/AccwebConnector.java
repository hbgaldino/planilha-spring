package com.planilha.connector;

import com.planilha.mapper.ActivityMapper;
import com.planilha.model.ActivityListVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AccwebConnector {

    private static final String OS_USERNAME = "os_username";
    private static final String OS_PASSWORD = "os_password";
    private static final String OS_COOKIE = "os_cookie";
    private static final String OS_DESTINATION = "os_destination";

    private static final String URL_LOGIN = "https://lab.accurate.com.br/request/login.jsp";
    private static final String URL_GET = "https://lab.accurate.com.br/accweb/ativ_inclusao.asp?pagResumo=true&Data=";
    private static final String URL_HOME = "https://lab.accurate.com.br/accweb/";

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    private RestTemplate restTemplate;

    public AccwebConnector() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setCircularRedirectsAllowed(true)
                .build();

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = new RestTemplate(factory);

    }

    private void request(String url, HttpMethod method, HttpEntity entity) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, entity, String.class);
        String location = responseEntity.getHeaders().getFirst(HttpHeaders.LOCATION);

        if (location != null) {
            HttpEntity httpEntity = new HttpEntity(null);
            request(location, HttpMethod.GET, httpEntity);
        }
    }

    public void authenticate(String username, String password) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(OS_USERNAME, username);
        formData.add(OS_PASSWORD, password);
        formData.add(OS_COOKIE, "true");
        formData.add(OS_DESTINATION, "\\loginAccProjetos.jsp");

        HttpEntity<MultiValueMap<String, String>> httpPostEntity = new HttpEntity<>(formData, null);

        request(URL_LOGIN, HttpMethod.POST, httpPostEntity);
    }


    public ActivityListVO fetch(Date date) {
        ActivityListVO activityListVO = new ActivityListVO();
        activityListVO.setDate(date);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL_GET.concat(sdf.format(date)), String.class);
        activityListVO.setActivityList(ActivityMapper.map(responseEntity.getBody()));
        return activityListVO;
    }
}
