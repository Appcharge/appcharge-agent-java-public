package com.appcharge.server.service.impl;

import com.appcharge.server.exception.ErrorResponse;
import com.appcharge.server.models.player.PlayerInfoSyncRequest;
import com.appcharge.server.models.player.PublisherPayload;
import com.appcharge.server.service.PlayerService;
import com.appcharge.server.service.SignatureGenerationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Random;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Value("${offer.update.publisherToken}")
    private String publisherToken;

    @Value("${AWARD_PUBLISHER_URL}")
    private String awardPublisherURL;
    private final ObjectMapper objectMapper;
    private final SignatureGenerationService signatureGenerationService;
    private final String playerDatasetFilePath;
    private final RestTemplate restTemplate;

    public PlayerServiceImpl(SignatureGenerationService signatureGenerationService,
                             @Value("${PLAYER_DATASET_FILE_PATH}") String playerDatasetFilePath,
                             RestTemplateBuilder restTemplateBuilder) {
        this.signatureGenerationService = signatureGenerationService;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.playerDatasetFilePath = playerDatasetFilePath;
        if (restTemplateBuilder == null) {
            throw new IllegalArgumentException("RestTemplateBuilder must not be null!");
        }
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public ResponseEntity<?> updateBalance(String body) throws Exception {
        try {
            PublisherPayload publisherPayload = objectMapper.readValue(body, PublisherPayload.class);

            if (publisherPayload == null) {
                throw new Exception("Could not parse publisher payload");
            }

            System.out.println(body);
            HttpHeaders headers = createHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JsonNode payloadNode = objectMapper.readTree(body); // Parse the body string into a JsonNode
            headers.add("signature", signatureGenerationService.generateSignature(payloadNode.toString()));
            HttpEntity<JsonNode> entity = new HttpEntity<>(payloadNode, headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(awardPublisherURL, HttpMethod.POST, entity, JsonNode.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            HttpStatus status = e.getStatusCode();
            String errorMessage = e.getResponseBodyAsString();
            JsonNode errorBody = objectMapper.readTree(errorMessage);
            return ResponseEntity.status(status).body(errorBody);
        } catch (Exception e) {
            // Log the error message and stack trace
            e.printStackTrace();

            String errorMessage = "An internal server error occurred.";
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();
            String responseBody = errorMessage + "\n" + stackTrace;

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @Override
    public ResponseEntity<?> playerInfoSync(@RequestBody String body) throws Exception {
        PlayerInfoSyncRequest request = objectMapper.readValue(body, PlayerInfoSyncRequest.class);

        if (request == null) {
            throw new Exception("Could not parse player info sync data");
        }


        JsonNode playerData = loadPlayerData().get("player");

        if (request.getSessionMetadata() == null) {
            ((ObjectNode) playerData).remove("sessionMetadata");
        }


        if (playerData != null) {
            updateBalances(playerData);
            String playerDataString = objectMapper.writeValueAsString(playerData);
            System.out.println(playerDataString); // Print the PlayerData for the given playerId
            return ResponseEntity.ok(playerData);
        } else {
            String errorMessage = "Player with ID " + playerData.get("publisherPlayerId") + " not found.";
            System.out.println(errorMessage);
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
        }
    }
    private static void updateBalances(JsonNode playerData) {
        JsonNode balancesArray = playerData.get("balances");
        for (JsonNode balanceNode : balancesArray) {
            if (balanceNode.has("balance")) {
                int newBalance = new Random().nextInt(100);
                ((ObjectNode) balanceNode).put("balance", newBalance);
            }
        }
    }

    // Method to load the player data from the dataset
    private JsonNode loadPlayerData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(playerDatasetFilePath);

        try (InputStream inputStream = new FileInputStream(file)) {
            return objectMapper.readTree(inputStream);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-publisher-token", publisherToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
