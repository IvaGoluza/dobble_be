package com.hr.fer.dobble.controller;

import com.hr.fer.dobble.dto.GameCreateResponse;
import com.hr.fer.dobble.dto.GameJoinResponse;
import com.hr.fer.dobble.dto.GameLobbyResponse;
import com.hr.fer.dobble.dto.PlayerScoreResponse;
import com.hr.fer.dobble.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    /**
     * Endpoint to create a new game with a creator player.
     *
     * @param creatorName   name of the player creating the game
     * @return              gameCode and creatorPlayerId
     */
    @PostMapping("/create")
    public ResponseEntity<GameCreateResponse> createGame(@RequestParam String creatorName) {
        GameCreateResponse response = gameService.createGame(creatorName);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Endpoint to join an existing game.
     *
     * @param gameCode    game code to join
     * @param playerName  name of the player joining
     * @return            playerId
     */
    @PostMapping("/{gameCode}/join")
    public ResponseEntity<GameJoinResponse> joinGame(
            @PathVariable String gameCode,
            @RequestParam String playerName
    ) {
        GameJoinResponse response = gameService.joinGame(gameCode, playerName);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to start a game.
     *
     * @param gameCode  game code to start
     * @return          HTTP 204 No Content
     */
    @PostMapping("/{gameCode}/start")
    public ResponseEntity<Void> startGame(@PathVariable String gameCode) {
        gameService.startGame(gameCode);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to take the top card by a player.
     *
     * @param gameCode       game code
     * @param playerId       ID of the player taking the card
     * @param playerSymbol   symbol selected by player
     * @return               HTTP 204 No Content
     */
    @PostMapping("/{gameCode}/take")
    public ResponseEntity<Void> takeTopCard(
            @PathVariable String gameCode,
            @RequestParam Long playerId,
            @RequestParam String playerSymbol
    ) {
        gameService.takeTopCard(gameCode, playerId, playerSymbol);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to retrieve final scores of a game.
     *
     * @param gameCode  game code
     * @return          list of player scores
     */
    @GetMapping("/{gameCode}/scores")
    public ResponseEntity<List<PlayerScoreResponse>> getScores(@PathVariable String gameCode) {
        List<PlayerScoreResponse> scores = gameService.getScores(gameCode);
        return ResponseEntity.ok(scores);
    }

    /**
     * Endpoint to retrieve current lobby players.
     *
     * @param gameCode  game code
     * @return          list of player names
     */
    @GetMapping("/{gameCode}/lobby")
    public ResponseEntity<GameLobbyResponse> getLobbyPlayers(@PathVariable String gameCode) {
        GameLobbyResponse response = gameService.getLobbyPlayers(gameCode);
        return ResponseEntity.ok(response);
    }
}
