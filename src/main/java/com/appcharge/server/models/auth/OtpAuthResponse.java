package com.appcharge.server.models.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class OtpAuthResponse {
    private final String deepLink;
    private final String accessToken;
}
