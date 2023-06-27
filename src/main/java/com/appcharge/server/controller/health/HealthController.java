package com.appcharge.server.controller.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/mocker/health")
    public String health() {
        return "OK";
    }
}
