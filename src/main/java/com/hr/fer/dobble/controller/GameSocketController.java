package com.hr.fer.dobble.controller;

import com.hr.fer.dobble.dto.ReconnectRequest;
import com.hr.fer.dobble.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class GameSocketController {

    private final GameService gameService;

    @MessageMapping("/reconnect")
    public void handleReconnect(ReconnectRequest reconnectRequest) {
        String gameCode = reconnectRequest.gameCode();
        Long playerId = reconnectRequest.playerId();

        gameService.getGameStateForPlayer(gameCode, playerId);
    }
}
