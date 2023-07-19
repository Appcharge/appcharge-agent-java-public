package com.appcharge.server.controller.order;

import com.appcharge.server.service.OrderService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/mocker/orders")
    public ResponseEntity<?> getOrdersEndpoint(@RequestBody String body) throws Exception {
        return orderService.getOrders(body);
    }
}

