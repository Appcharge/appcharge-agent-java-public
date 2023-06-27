package com.appcharge.server.models.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AuthResponse {
    private final String status;
    private final String playerProfileImage;
    private final String publisherPlayerId;
    private final String playerName;
    private final List<String> segments;
    private final List<ItemBalance> balances;
}
