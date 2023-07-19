package com.appcharge.server.service.impl;

import com.appcharge.server.controller.auth.methods.AppleAuth;
import com.appcharge.server.controller.auth.methods.FacebookAuth;
import com.appcharge.server.controller.auth.methods.GoogleAuth;
import com.appcharge.server.exception.BadRequestException;
import com.appcharge.server.models.auth.AuthResponse;
import com.appcharge.server.models.auth.AuthResult;
import com.appcharge.server.models.auth.AuthenticationRequest;
import com.appcharge.server.models.auth.ItemBalance;
import com.appcharge.server.service.AuthService;
import com.appcharge.server.service.SecretsService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    @SneakyThrows
    public AuthResponse authenticatePlayer(AuthenticationRequest authRequest, SecretsService secretsService) {
        AuthResult authResult = new AuthResult(false, "0");

        String authMethod = authRequest != null ? authRequest.getAuthMethod() : null;
        if (Objects.requireNonNull(authMethod).equals("facebook")) {
            authResult = FacebookAuth.authenticate(authRequest.getAppId(), authRequest.getToken(),
                    secretsService.getFacebookSecret());
        } else if (Objects.requireNonNull(authMethod).equals("google")) {
            authResult = GoogleAuth.authenticate(authRequest.getAppId(), authRequest.getToken());
        } else if (Objects.requireNonNull(authMethod).equals("apple")) {
            authResult = AppleAuth.authenticate(authRequest.getAppId(), authRequest.getToken(), secretsService.getAppleSecretApi());
        } else if (Objects.requireNonNull(authMethod).equals("userToken")) {
            authResult = new AuthResult(true, authRequest.getToken());
        } else if (Objects.requireNonNull(authMethod).equals("userPassword")) {
            authResult = new AuthResult(true, authRequest.getToken());
        }

        if (authResult.getIsValid()) {
            return createAuthResponse(authResult);
        } else {
            System.out.println("Unknown authentication method " + authMethod);
            throw new BadRequestException();
        }
    }

    private AuthResponse createAuthResponse(AuthResult authResult) {
        return new AuthResponse(
                "valid",
                "https://scontent.ftlv15-1.fna.fbcdn.net/v/t1.6435-9/39453230_281250465987441_6821580385961377792_n.jpg?_nc_cat=101&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=EhNdfDjnT0MAX9Urj2h&_nc_ht=scontent.ftlv15-1.fna&oh=00_AfDz7cKzDCQC17o4L0i1ujpjilH11pTdfVyWPXMxzuOGxQ&oe=64C269BF",
                authResult.getUserId(),
                "Joe Dow",
                List.of("seg1", "seg2"),
                List.of(new ItemBalance("diamonds", 15))
        );
    }
}
