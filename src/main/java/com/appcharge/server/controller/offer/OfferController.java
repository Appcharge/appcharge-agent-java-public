package com.appcharge.server.controller.offer;

import com.appcharge.server.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping("/mocker/offer")
    public ResponseEntity<?> createOffer() throws IOException {
        return offerService.createOffer();
    }

    @PutMapping("/mocker/offer")
    public ResponseEntity<?> updateOffer() throws IOException {
        return offerService.updateOffer();
    }
}
