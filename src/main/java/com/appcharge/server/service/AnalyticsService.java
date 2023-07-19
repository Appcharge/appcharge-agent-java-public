package com.appcharge.server.service;

import org.springframework.http.ResponseEntity;

public interface AnalyticsService {
    ResponseEntity<?> getAnalytics(String body) throws Exception;
}
