package com.appcharge.server.service;

import com.appcharge.server.models.auth.AuthResponse;
import com.appcharge.server.models.auth.AuthenticationRequest;

public interface AuthService {
    AuthResponse authenticatePlayer(AuthenticationRequest authRequest, SecretsService secretsService);
}