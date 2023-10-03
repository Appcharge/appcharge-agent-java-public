package com.appcharge.server.controller.auth.methods;

import com.appcharge.server.models.auth.AuthResult;

public interface AppleAuth {
    AuthResult authenticate(String appId, String token, String appleSecretApi);
}
