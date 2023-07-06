package com.appcharge.server.models.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthResult {
    private final Boolean isValid;
    private final String publisherErrorMessage;
}
