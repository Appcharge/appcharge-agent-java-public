package com.appcharge.server.service;

import com.appcharge.server.models.events.EventsRequest;
import com.appcharge.server.models.events.EventsResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

public interface EventsService {

    EventsResponse processEvent(EventsRequest body) throws Exception;
}
