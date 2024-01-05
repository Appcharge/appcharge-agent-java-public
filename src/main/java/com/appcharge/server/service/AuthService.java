package com.appcharge.server.service;

import com.appcharge.server.models.auth.AuthResponse;
import com.appcharge.server.models.auth.AuthenticationRequest;
import com.appcharge.server.models.auth.OtpAuthResponse;
import com.appcharge.server.models.auth.OtpAuthenticationRequest;

public interface AuthService {
    AuthResponse authenticatePlayer(AuthenticationRequest authRequest, SecretsService secretsService);
    OtpAuthResponse authenticatePlayerOtp(OtpAuthenticationRequest otpAuthRequest);
}
