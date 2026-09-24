package com.scm.Services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.scm.Services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final RestClient restClient;

    @Value("${brevo.api-key}")
    private String brevoApiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    @Value("${brevo.sender-name:ContactSphere}")
    private String senderName;

    public EmailServiceImpl() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.brevo.com")
                .build();
    }

    @Override
    public void sendEmail(String to, String subject, String body) {

        Map<String, Object> requestBody = Map.of(
                "sender", Map.of(
                        "name", senderName,
                        "email", senderEmail
                ),
                "to", List.of(
                        Map.of(
                                "email", to
                        )
                ),
                "subject", subject,
                "textContent", body
        );

        restClient.post()
                .uri("/v3/smtp/email")
                .header("api-key", brevoApiKey)
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();

        System.out.println("Email sent successfully to " + to);
    }

    @Override
    public void sendEmailWithHtml() {

    }

    @Override
    public void sendEmailWithAttachment() {

    }
}