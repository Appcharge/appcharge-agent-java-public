package com.appcharge.server.service;

import org.springframework.stereotype.Service;

import com.appcharge.server.service.entity.SignatureResponse;

@Service
public interface SignatureHashingService {
    SignatureResponse signPayload(String expectedSignature, String data);
}
