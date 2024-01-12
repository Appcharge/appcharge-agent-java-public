package com.appcharge.server.models.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OtpDeepLinkGenerationResponse {
    private final String deepLink;
    private final String accessToken;
}
