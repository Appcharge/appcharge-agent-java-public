package com.appcharge.server.models.player;

import java.util.Map;

public class PlayersData {
    private Map<String, PlayerData> players;

    public Map<String, PlayerData> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, PlayerData> players) {
        this.players = players;
    }
}
