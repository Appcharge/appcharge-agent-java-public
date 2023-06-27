package com.appcharge.server.service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface OfferService {
    ResponseEntity<?> createOffer() throws IOException;

    ResponseEntity<?> updateOffer() throws IOException;
}

