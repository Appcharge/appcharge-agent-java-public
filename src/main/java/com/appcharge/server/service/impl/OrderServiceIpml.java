package com.appcharge.server.service.impl;

import com.appcharge.server.models.orders.OrdersRequest;
import com.appcharge.server.service.OrderService;
import com.appcharge.server.service.SignatureGenerationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderServiceIpml implements OrderService {
    @Value("${REPORTING_API_URL}/reporting/reports/orders")
    private String getOrdersUrl;

    @Value("${offer.update.publisherToken}")
    private String publisherToken;

    private final ObjectMapper objectMapper;

    private final SignatureGenerationService signatureGenerationService;

    public OrderServiceIpml(SignatureGenerationService signatureGenerationService) {
        this.signatureGenerationService = signatureGenerationService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ResponseEntity<?> getOrders(@RequestBody String body) throws Exception {
        OrdersRequest request = objectMapper.readValue(body, OrdersRequest.class);

        if (request == null) {
            throw new Exception("Could not parse orders data");
        }
        System.out.println(body);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonNode payloadNode = objectMapper.readTree(body); // Parse the body string into a JsonNode
        headers.add("signature", signatureGenerationService.generateSignature(payloadNode.toString()));
        HttpEntity<JsonNode> entity = new HttpEntity<>(payloadNode, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(getOrdersUrl, HttpMethod.POST, entity, JsonNode.class);
        return ResponseEntity.ok(response.getBody());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-publisher-token", publisherToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
