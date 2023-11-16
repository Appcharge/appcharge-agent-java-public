package com.appcharge.server.service;

import com.appcharge.server.models.events.EventsRequest;
import com.appcharge.server.models.events.EventsResponse;

public interface EventsService {

    EventsResponse processEvent(EventsRequest body) throws Exception;
}
