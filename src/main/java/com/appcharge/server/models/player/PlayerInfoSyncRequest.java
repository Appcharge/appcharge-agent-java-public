package com.appcharge.server.models.player;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PlayerInfoSyncRequest {
    private String playerId;
    private Map<String, Object> sessionMetadata;
}
