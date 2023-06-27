package com.appcharge.server.service.impl;

import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.appcharge.server.service.OfferService;
import com.appcharge.server.service.SignatureGenerationService;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
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

    public OfferServiceImpl(SignatureGenerationService signatureGenerationService) {
        this.signatureGenerationService = signatureGenerationService;
        this.offerDataset = new ClassPathResource("offers.json");
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ResponseEntity<?> createOffer() throws IOException {
        try {
            RestTemplate restTemplate = new RestTemplate();
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
            RestTemplate restTemplate = new RestTemplate();
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
        String filePath = "./src/main/resources/offers.json";
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);

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
            String filePath = "./src/main/resources/player-dataset.json";
            ObjectMapper objectMapper = new ObjectMapper();
            File playerDatasetFile = new File(filePath);

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
