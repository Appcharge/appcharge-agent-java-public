package com.appcharge.server.controller.player;

import org.springframework.http.ResponseEntity;
import com.appcharge.server.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping("/mocker/updateBalance")
    public ResponseEntity<?> playerUpdateBalanceEndpoint(@RequestBody String body) throws Exception {
        return playerService.updateBalance(body);
    }

    @PostMapping("/mocker/playerInfoSync")
    public ResponseEntity<?> playerInfoSyncEndpoint(@RequestBody String body) throws Exception {
        return playerService.playerInfoSync(body);
    }
}
