package com.appcharge.server.service;

import com.appcharge.server.service.entity.SignatureResponse;
import org.springframework.stereotype.Service;

@Service
public interface SignatureHashingService {
    SignatureResponse signPayload(String expectedSignature, String data);
}
