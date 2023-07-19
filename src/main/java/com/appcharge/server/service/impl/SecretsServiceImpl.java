package com.appcharge.server.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appcharge.server.service.SecretsService;

@Service
public class SecretsServiceImpl implements SecretsService {

    private final String facebookAppSecret;
    private final String appleSecretApi;

    public SecretsServiceImpl(@Value("${FACEBOOK_APP_SECRET}") String facebookAppSecret, @Value("${APPLE_SECRET_API}") String appleSecretApi) {
        this.facebookAppSecret = facebookAppSecret;
        this.appleSecretApi = appleSecretApi;
    }

    public String getFacebookSecret() {
        return facebookAppSecret;
    }

    public String getAppleSecretApi() { return appleSecretApi; }
}
