package com.appcharge.server.service.impl;

import com.appcharge.server.models.analytics.AnalyticsRequest;
import com.appcharge.server.service.AnalyticsService;
import com.appcharge.server.service.SignatureGenerationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    @Value("${REPORTING_API_URL}/reporting/reports/analytics")
    private String getAnalyticsUrl;

    @Value("${offer.update.publisherToken}")
    private String publisherToken;

    private final ObjectMapper objectMapper;

    private final SignatureGenerationService signatureGenerationService;

    public AnalyticsServiceImpl(SignatureGenerationService signatureGenerationService) {
        this.signatureGenerationService = signatureGenerationService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ResponseEntity<?> getAnalytics(@RequestBody String body) throws Exception {
        AnalyticsRequest request = objectMapper.readValue(body, AnalyticsRequest.class);

        if (request == null) {
            throw new Exception("Could not parse analytics data");
        }
        System.out.println(body);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonNode payloadNode = objectMapper.readTree(body); // Parse the body string into a JsonNode
        headers.add("signature", signatureGenerationService.generateSignature(payloadNode.toString()));
        HttpEntity<JsonNode> entity = new HttpEntity<>(payloadNode, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(getAnalyticsUrl, HttpMethod.POST, entity, JsonNode.class);
        return ResponseEntity.ok(response.getBody());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-publisher-token", publisherToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
