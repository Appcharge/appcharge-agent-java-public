package com.appcharge.server.service.impl;

import com.appcharge.server.exception.BadRequestException;
import com.appcharge.server.models.auth.AuthResponse;
import com.appcharge.server.models.auth.AuthResult;
import com.appcharge.server.models.auth.ItemBalance;
import com.appcharge.server.models.events.EventsRequest;
import com.appcharge.server.models.events.EventsResponse;
import com.appcharge.server.service.EventsService;
import com.appcharge.server.service.SignatureGenerationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class EventsServiceImpl implements EventsService {
    private final String eventsDatasetFilePath;

    public EventsServiceImpl(@Value("${EVENTS_DATASET_FILE_PATH}") String eventsDatasetFilePath) {
        this.eventsDatasetFilePath = eventsDatasetFilePath;
    }

    @Override
    @SneakyThrows
    public EventsResponse processEvent(EventsRequest eventsRequest) throws Exception {
        try {
            return new EventsResponse("An event was saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
;
    private JsonNode loadEventsData(File eventsDatasetFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = new FileInputStream(eventsDatasetFile)) {
            return objectMapper.readTree(inputStream);
        } catch (Exception e) {
            return null;
        }
    }
    private void writeDataToFile(EventsRequest eventsRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File(eventsDatasetFilePath);
        try {
            JsonNode tempNode = loadEventsData(file);
            ArrayNode rootNode = (ArrayNode) tempNode.get(eventsRequest.event.toString());
            rootNode.add(objectMapper.convertValue(eventsRequest, JsonNode.class));

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tempNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
