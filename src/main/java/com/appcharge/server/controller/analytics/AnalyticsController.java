package com.appcharge.server.controller.analytics;

import com.appcharge.server.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @PostMapping("/mocker/analytics")
    public ResponseEntity<?> getAnalyticsEndpoint(@RequestBody String body) throws Exception {
        return analyticsService.getAnalytics(body);
    }
}
