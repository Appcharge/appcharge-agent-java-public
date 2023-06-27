package com.appcharge.server.models.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest {
    private String authMethod;
    private String token;
    private LocalDateTime date;
    private String appId;
    private String userName;
    private String password;
}
