package com.appcharge.server.controller.auth.methods;

import com.appcharge.server.models.auth.AuthResult;

public interface FacebookAuth {
    AuthResult authenticate(String appId, String token, String facebookSecret);
}
