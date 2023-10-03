package com.appcharge.server.service.impl;

import com.appcharge.server.service.OfferService;
import com.appcharge.server.service.SignatureGenerationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Service
public class OfferServiceImpl implements OfferService {

    @Value("${ASSET_UPLOAD_GATEWAY_URL}/offering/offer/")
    private String updateOfferUrl;

    @Value("${ASSET_UPLOAD_GATEWAY_URL}/offering/offer/")
    private String createOfferUrl;

    @Value("${offer.update.publisherToken}")
    private String publisherToken;

    private Resource offerDataset;
    private final ObjectMapper objectMapper;
    private final SignatureGenerationService signatureGenerationService;

    private final String offersFilePath;
    private final String playerDatasetFilePath;
    private final RestTemplate restTemplate;

    public OfferServiceImpl(SignatureGenerationService signatureGenerationService, @Value("${OFFERS_FILE_PATH}") String offersFilePath, @Value("${PLAYER_DATASET_FILE_PATH}") String playerDatasetFilePath, RestTemplateBuilder restTemplateBuilder) {
        this.signatureGenerationService = signatureGenerationService;
        this.offerDataset = new ClassPathResource(offersFilePath);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.offersFilePath = offersFilePath;
        this.playerDatasetFilePath = playerDatasetFilePath;
        if (restTemplateBuilder == null) {
            throw new IllegalArgumentException("RestTemplateBuilder must not be null!");
        }
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public ResponseEntity<?> createOffer() throws IOException {
        try {
            HttpHeaders headers = createHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JsonNode offerDataset = loadOffers().get("create");
            headers.add("signature", signatureGenerationService.generateSignature(offerDataset.toString()));
            HttpEntity<JsonNode> entity = new HttpEntity<>(offerDataset, headers);

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    createOfferUrl,
                    HttpMethod.POST,
                    entity,
                    JsonNode.class
            );
            JsonNode responseBody = response.getBody();
            updateOfferId();
            assignNewOrderToPlayers(responseBody);
            return ResponseEntity.ok(responseBody);
        } catch (HttpClientErrorException e) {
            HttpStatus status = e.getStatusCode();
            String errorMessage = e.getResponseBodyAsString();
            JsonNode errorBody = objectMapper.readTree(errorMessage);
            return ResponseEntity.status(status).body(errorBody);
        } catch (IOException e) {
            // Handle other IOExceptions appropriately
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<?> updateOffer() {
        try {
            HttpHeaders headers = createHeaders();
            JsonNode offerDataset = loadOffers().get("update");
            String offerId = offerDataset.get("publisherOfferId").asText();
            String reqUrl = updateOfferUrl + "/" + offerId;
            JsonNode modifiedOfferDataset = removeFields(offerDataset, "publisherOfferId", "createdBy", "intervals");
            headers.add("signature", signatureGenerationService.generateSignature(modifiedOfferDataset.toString()));
            HttpEntity<String> entity = new HttpEntity<>(modifiedOfferDataset.toString(), headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(reqUrl, HttpMethod.PUT, entity, JsonNode.class);
            HttpStatus statusCode = response.getStatusCode();
            JsonNode responseBody = response.getBody();

            HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(responseBody, jsonHeaders, statusCode);
        } catch (IOException e) {
            // Handle the exception appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private JsonNode loadOffers() throws IOException {
        try (InputStream inputStream = offerDataset.getInputStream()) {
            return objectMapper.readTree(inputStream);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-publisher-token", publisherToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private JsonNode removeFields(JsonNode jsonNode, String... fieldNames) {
        ObjectNode objectNode = (ObjectNode) jsonNode;
        for (String fieldName : fieldNames) {
            objectNode.remove(fieldName);
        }
        return objectNode;
    }

    private void updateOfferId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(offersFilePath);

        // Read the JSON from the file
        JsonNode jsonNode = objectMapper.readTree(file);

        String originalOfferId = jsonNode.get("create").get("publisherOfferId").asText();
        String newOfferId = originalOfferId + "1";
        ((ObjectNode) jsonNode.get("create")).put("publisherOfferId", newOfferId);
        // Write the updated JSON back to the file
        objectMapper.writeValue(file, jsonNode);

        // Refresh the offerDataset variable with the updated data
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        offerDataset = new ByteArrayResource(fileBytes);
    }


    private void assignNewOrderToPlayers(JsonNode response) throws IOException {
        try {
            // Load player dataset JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            File playerDatasetFile = new File(playerDatasetFilePath);

            // Read the JSON from the file
            JsonNode playerDataset = objectMapper.readTree(playerDatasetFile);

            JsonNode newOrderJson = createMockNewOrder(response);

            // Iterate over each player
            JsonNode player = playerDataset.get("player");
            if (player instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) player;
                // Add a new order to the player's "offers" array
                ArrayNode offers = (ArrayNode) player.get("offers");
                if (offers == null) {
                    offers = objectMapper.createArrayNode();
                    objectNode.set("offers", offers);
                }
                offers.add(newOrderJson);
            }

            // Write the updated player dataset back to the file
            objectMapper.writeValue(playerDatasetFile, playerDataset);

            System.out.println("New order assigned to players successfully. Updated player dataset saved to file: " + playerDatasetFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonNode createMockNewOrder(JsonNode response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode newOrder = objectMapper.createObjectNode();

        newOrder.put("publisherOfferId", response.get("publisherOfferId").asText()); // Use the offerId from the response

        ArrayNode productsSequences = objectMapper.createArrayNode();
        ObjectNode productsSequence = objectMapper.createObjectNode();
        productsSequence.put("index", 1);
        productsSequences.add(productsSequence);

        newOrder.set("productsSequence", productsSequences);

        return newOrder;
    }
}
