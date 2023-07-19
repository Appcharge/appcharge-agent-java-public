package com.appcharge.server.service;

import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<?> getOrders(String body) throws Exception;

}
