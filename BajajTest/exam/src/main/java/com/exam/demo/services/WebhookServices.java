package com.exam.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.exam.demo.dto.FinalQueryRequest;
import com.exam.demo.dto.WebhookRequest;
import com.exam.demo.dto.WebhookResponse;

@Service
public class WebhookServices {
	
    private static final String GENERATE_WEBHOOK_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private final RestTemplate restTemplate = new RestTemplate();

    public void executeFlow() {

  
        WebhookRequest request =
                new WebhookRequest("Adhyyan Chaturvedi", "250850120012", "adhyyanchaturvedi10@gamil.com");

        WebhookResponse response =
                restTemplate.postForObject(GENERATE_WEBHOOK_URL, request, WebhookResponse.class);

        if (response == null) {
            throw new RuntimeException("Webhook generation failed");
        }

        String webhookUrl = response.getWebhook();
        String accessToken = response.getAccessToken();

       

      
        String finalQuery ="SELECT d.DEPARTMENT_NAME, " +
        		"ROUND(AVG(TIMESTAMPDIFF(YEAR, e.DOB, CURDATE())), 2) AS AVERAGE_AGE, " +
        		"SUBSTRING_INDEX(" +
        		"GROUP_CONCAT(DISTINCT CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) " +
        		"ORDER BY e.EMP_ID SEPARATOR ', '), ', ', 10) AS EMPLOYEE_LIST " +
        		"FROM DEPARTMENT d " +
        		"JOIN EMPLOYEE e ON e.DEPARTMENT = d.DEPARTMENT_ID " +
        		"JOIN PAYMENTS p ON p.EMP_ID = e.EMP_ID " +
        		"WHERE p.AMOUNT > 70000 " +
        		"GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME " +
        		"ORDER BY d.DEPARTMENT_ID DESC";
       
       
        submitFinalQuery(webhookUrl, accessToken, finalQuery);
    }

    private void submitFinalQuery(String webhookUrl, String token, String finalQuery) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        FinalQueryRequest body = new FinalQueryRequest(finalQuery);

        HttpEntity<FinalQueryRequest> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(webhookUrl, entity, String.class);

        System.out.println("Submission Response: " + response.getBody());
    }

}
