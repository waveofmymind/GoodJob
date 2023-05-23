package com.goodjob.domain.job;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.RestTemplate;

public class JobKoreaApiManager extends JobStatistic {

    private String key;

    public String findUser(String key) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String jsonString = restTemplate.getForObject(Constant.JOBKOREA, String.class);

        JSONParser jsonParser = new JSONParser();
        Object jsonObject = jsonParser.parse(jsonString);

        JSONObject jsonBody = (JSONObject) jsonObject;

        return jsonBody.get("handle").toString();
    }
}
