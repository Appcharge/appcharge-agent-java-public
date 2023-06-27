package com.appcharge.server.service.impl;

import com.appcharge.server.service.SignatureGenerationService;
import com.appcharge.server.service.SignatureHashingService;
import com.appcharge.server.service.entity.SignatureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignatureGenerationServiceImpl implements SignatureGenerationService {
    private final SignatureHashingService signatureHashingService;
    @Value("${KEY}")
    private String encryptionKey;

    @Override
    public String generateSignature(String data) {
        long timestamp = System.currentTimeMillis();
        String expectedSignatureHeader = String.format("t=%d,v1=placeholder", timestamp);
        SignatureResponse signatureResponse = signatureHashingService.signPayload(expectedSignatureHeader, data);
        String signature = signatureResponse.getSignature();
        System.out.println(signature);
        return String.format("t=%d,v1=%s", timestamp, signature);
    }
}
