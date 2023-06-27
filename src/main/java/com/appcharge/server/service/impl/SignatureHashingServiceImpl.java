package com.appcharge.server.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appcharge.server.exception.TimestampExpiredException;
import com.appcharge.server.service.SignatureHashingService;
import com.appcharge.server.service.entity.Signature;
import com.appcharge.server.service.entity.SignatureResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SignatureHashingServiceImpl implements SignatureHashingService {
    private final byte[] key;

    public SignatureHashingServiceImpl(@Value("${KEY}") String key) {
        this.key = key.getBytes(StandardCharsets.US_ASCII);
    }

    private Signature parseSignature(String signatureString) {
        Pattern pattern = Pattern.compile("t=(.*),v1=(.*)");
        Matcher matcher = pattern.matcher(signatureString);

        if (!matcher.matches() || matcher.groupCount() < 2) {
            throw new IllegalArgumentException("Invalid signature format");
        }

        String t = matcher.group(1);
        String v1 = matcher.group(2);

        long timestamp = Long.parseLong(t);
        long currentTime = System.currentTimeMillis();
        long elapsedTimeMinutes = (currentTime - timestamp) / (1000 * 60);

        if (elapsedTimeMinutes > 5) {
            throw new TimestampExpiredException("Timestamp is older than 5 minutes");
        }

        return new Signature(t, v1);
    }

    @Override
    public SignatureResponse signPayload(String expectedSignatureHeader, String data) {
        Signature expectedSignature = parseSignature(expectedSignatureHeader);
        byte[] dataBytes = String.format("%s.%s", expectedSignature.getT(), data).getBytes(StandardCharsets.UTF_8);

        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
            hmac.init(secretKey);
            byte[] hmacBytes = hmac.doFinal(dataBytes);
            String signature = bytesToHex(hmacBytes).toLowerCase();
            return new SignatureResponse(signature, expectedSignature.getV1());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
