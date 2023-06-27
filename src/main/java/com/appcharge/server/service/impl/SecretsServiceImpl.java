package com.appcharge.server.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appcharge.server.service.SecretsService;

@Service
public class SecretsServiceImpl implements SecretsService {

    private final String facebookAppSecret;

    public SecretsServiceImpl(@Value("${FACEBOOK_APP_SECRET}") String facebookAppSecret) {
        this.facebookAppSecret = facebookAppSecret;
    }

    public String getFacebookSecret() {
        return facebookAppSecret;
    }
}
