package com.appcharge.server.service;

import com.appcharge.server.models.auth.AuthResponse;
import com.appcharge.server.models.auth.AuthenticationRequest;
import com.appcharge.server.models.auth.OtpDeepLinkGenerationResponse;
import com.appcharge.server.models.auth.OtpDeepLinkGenerationRequest;

public interface AuthService {
    AuthResponse authenticatePlayer(AuthenticationRequest authRequest, SecretsService secretsService);
    OtpDeepLinkGenerationResponse generateOtpDeepLink(OtpDeepLinkGenerationRequest otpDeepLinkGenerationRequest);
}
