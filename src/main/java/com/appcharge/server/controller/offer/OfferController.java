package com.appcharge.server.controller.offer;

import com.appcharge.server.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping("/mocker/offer")
    public ResponseEntity<?> createOffer() {
        try {
            return offerService.createOffer();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping("/mocker/offer")
    public ResponseEntity<?> updateOffer() {
        try {
            return offerService.updateOffer();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
