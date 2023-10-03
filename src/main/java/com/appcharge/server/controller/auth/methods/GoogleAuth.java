package com.appcharge.server.controller.auth.methods;

import com.appcharge.server.models.auth.AuthResult;

public interface GoogleAuth {
    AuthResult authenticate(String appId, String token);
}
