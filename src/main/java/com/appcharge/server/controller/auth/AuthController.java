package com.appcharge.server.controller.auth;

import com.appcharge.server.models.auth.AuthResponse;
import com.appcharge.server.models.auth.AuthenticationRequest;
import com.appcharge.server.models.auth.OtpDeepLinkGenerationResponse;
import com.appcharge.server.models.auth.OtpDeepLinkGenerationRequest;
import com.appcharge.server.service.AuthService;
import com.appcharge.server.service.SecretsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/mocker/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SecretsService secretsService;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> playerAuth(@RequestBody String requestBody) {
        AuthenticationRequest authRequest;
        try {
            authRequest = objectMapper.readValue(requestBody, AuthenticationRequest.class);
        } catch (JsonProcessingException e) {
            System.out.println("Invalid request " + requestBody);
            return ResponseEntity.badRequest().body(null);
        }

        AuthResponse authResponse = authService.authenticatePlayer(authRequest, secretsService);

        if (authResponse != null) {
            System.out.println("Successful login");
            return ResponseEntity.ok(authResponse);
        }

        System.out.println("Failed login");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/otp/deeplink")
    public ResponseEntity<?> generateOtpDeepLink(@RequestBody String body) {
        OtpDeepLinkGenerationRequest otpDeepLinkGenerationRequest;
        try {
            otpDeepLinkGenerationRequest = objectMapper.readValue(body, OtpDeepLinkGenerationRequest.class);
        } catch (JsonProcessingException e) {
            System.out.println("Invalid request " + body);
            return ResponseEntity.badRequest().body(null);
        }

        try {
            OtpDeepLinkGenerationResponse otpDeepLinkGenerationResponse = authService.generateOtpDeepLink(otpDeepLinkGenerationRequest);
            return ResponseEntity.ok(otpDeepLinkGenerationResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}
