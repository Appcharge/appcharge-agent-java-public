package com.appcharge.server.controller.events;

import com.appcharge.server.models.events.EventsRequest;
import com.appcharge.server.models.events.EventsResponse;
import com.appcharge.server.service.EventsService;
import com.appcharge.server.service.impl.EventsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventsController {
    private final EventsServiceImpl eventsService;
    private final ObjectMapper objectMapper;

    @PostMapping("/mocker/events")
    public ResponseEntity<EventsResponse> eventEndpoint(@RequestBody String requestBody) throws Exception {
        EventsRequest eventsRequest;
        try {
            eventsRequest = objectMapper.readValue(requestBody, EventsRequest.class);
        } catch (JsonProcessingException e) {
            System.out.println("Invalid request " + requestBody);
            return ResponseEntity.badRequest().body(null);
        }

        EventsResponse eventsResponse = eventsService.processEvent(eventsRequest);

        if (eventsResponse != null) {
            System.out.println("Event was save successfully");
            return ResponseEntity.ok(eventsResponse);
        }

        System.out.println("Failed to save an event!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
