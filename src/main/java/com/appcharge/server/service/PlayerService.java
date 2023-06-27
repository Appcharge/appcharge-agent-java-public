package com.appcharge.server.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface PlayerService {
    ResponseEntity<?> updateBalance(String body) throws Exception;

    ResponseEntity<?> playerInfoSync(@RequestBody String body) throws Exception;
}

