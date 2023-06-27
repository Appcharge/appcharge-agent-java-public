package com.appcharge.server.service.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignatureResponse {
    private final String signature;
    private final String expectedSignature;
}