package com.hr.fer.dobble.service;

import com.hr.fer.dobble.dto.*;
import com.hr.fer.dobble.model.Card;
import com.hr.fer.dobble.model.Game;
import com.hr.fer.dobble.model.Player;
import com.hr.fer.dobble.repository.GameRepository;
import com.hr.fer.dobble.socket.GameSocketNotifier;
import com.hr.fer.dobble.util.GameCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final PlayerService playerService;
    private final CardService cardService;
    private final GameSocketNotifier socketNotifier;
    private final GameCodeGenerator codeGenerator;

    /**
     * Method to create the Game, its Game Code and shuffled deck of cards
     *
     * @param creatorName name of the player that created the game
     * @return game code and Creator ID
     */
    public GameCreateResponse createGame(String creatorName) {
        List<Long> deck = cardService.generateCardDeck();

        Game game = Game.builder()
                .started(false)
                .gameCode(codeGenerator.generateUniqueGameCode())
                .players(new ArrayList<>())
                .build();
        game.setCardIdList(deck);
        game = gameRepository.save(game);

        Player creator = playerService.createPlayer(creatorName, deck.getFirst(), game);
        game.setCreator(creator);
        game.getPlayers().add(creator);

        gameRepository.save(game);

        return new GameCreateResponse(game.getGameCode(), creator.getId());
    }

    /**
     * Method to create a new Player for certain Game, assign a first card and add Player to the Game.
     *
     * @param gameCode      code of the game that player is joining
     * @param playerName    player name
     * @return              player ID
     */
    public GameJoinResponse joinGame(String gameCode, String playerName) {

        // Find the game by code
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new IllegalArgumentException("Game " + gameCode + " not found."));

        // Check if game has already started
        if (game.isStarted()) {
            throw new IllegalStateException("Cannot join a game that has already started.");
        }

        // Get the shuffled deck and ensure there's enough cards left
        List<Long> deck = game.getCardIdList();
        int assignedCount = game.getPlayers().size();
        if (assignedCount >= deck.size()) {
            throw new IllegalStateException("Not enough cards left to assign to new player.");
        }

        // Assign the next card from the deck to the joining player
        Player player = playerService.createPlayer(playerName, deck.get(assignedCount), game);
        game.getPlayers().add(player);
        gameRepository.save(game);

        List<String> playerNames = playerService.getPlayersByGameCode(gameCode).stream()
                .map(Player::getName)
                .toList();

        socketNotifier.notifyLobbyPlayers(gameCode, playerNames);

        return new GameJoinResponse(player.getId());
    }

    /**
     * Method to start the Game.
     * Notifies every player of the game start and sends them Top Card and their Card.
     *
     * @param gameCode  code of the game
     */
    @Transactional
    public void startGame(String gameCode) {
        // Find the game by code
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new IllegalArgumentException("Game " + gameCode + " not found"));

        // Check if game has already started
        if (game.isStarted()) {
            throw new IllegalStateException("Game already started");
        }

        // Get players and top card
        List<Long> deck = game.getCardIdList();
        List<Player> players = playerService.getPlayersByGameCode(gameCode);

        if (players.size() >= deck.size()) {
            throw new IllegalStateException("Not enough cards in deck to start the game");
        }

        int startingCardIndex = players.size();
        Long topCardId = deck.get(startingCardIndex);
        List<String> topCardSymbols = cardService.getCardSymbols(topCardId);

        // Mark game as started and set next top card index
        game.setStarted(true);
        game.setTopCardIndex(startingCardIndex);
        gameRepository.save(game);

        // Notify all players of game start and their assigned cards
        for (Player player : players) {
            List<String> playerCardSymbols = cardService.getCardSymbols(player.getCurrentCardId());

            socketNotifier.notifyGameStart(
                    gameCode,
                    player.getId(),
                    topCardId,
                    topCardSymbols,
                    player.getCurrentCardId(),
                    playerCardSymbols
            );
        }

    }

    /**
     * Method to process move of the fastest Player that took the Top Card from the Game deck.
     * Move is validated on the FE, and only valid request for moves are sent on BE.
     * Move is once more validated on BE and if valid, update Player score, set new Top Card for the Game
     * and notify the rest of the players.
     *
     * @param gameCode  code of the game
     * @param playerId  player ID
     * @param playerSymbol Symbol that player has chosen for the common Symbol with Top Card of the Game
     */
    @Transactional
    public void takeTopCard(String gameCode, Long playerId, String playerSymbol) {
        // Find the game and player
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new IllegalArgumentException("Game " + gameCode + " not found"));
        Player player = playerService.getPlayerById(playerId);

        List<Long> cardDeck = game.getCardIdList();
        int topCardIndex = game.getTopCardIndex();

        // Get Top Card Symbols
        Long topCardId = cardDeck.get(topCardIndex);
        List<String> topCardSymbols = cardService.getCardSymbols(topCardId);

        // Validate that clicked symbol exists on top card
        if (!topCardSymbols.contains(playerSymbol)) {
            throw new IllegalStateException("Invalid move: selected symbol " + playerSymbol + " not on top card. Top card symbols: " + topCardSymbols);
        }

        // Check if move has already been processed
        if (game.getTopCardIndex() != topCardIndex) {
            throw new IllegalStateException("Invalid move: someone else already took the top card.");
        }

        // Valid move - process
        player.setScore(player.getScore() + 1);
        player.setCurrentCardId(topCardId); // Give top card to this player
        game.setTopCardIndex(topCardIndex + 1); // Advance the deck

        gameRepository.save(game);
        playerService.savePlayer(player);

        // Check if last Card in the deck - game end
        if (topCardIndex + 1 == cardDeck.size()) {
            socketNotifier.notifyGameOver(gameCode);
            return;
        }

        // Get NEW Top Card Symbols
        Long newTopCardId = cardDeck.get(topCardIndex + 1);
        List<String> newTopCardSymbols = cardService.getCardSymbols(newTopCardId);

        // Notify all players via WebSocket
        socketNotifier.notifyCardWon(
                gameCode,
                player.getName(),
                newTopCardId,
                newTopCardSymbols,
                topCardId,
                topCardSymbols
        );
    }

    /**
     * Method to retrieve all Players for certain Game with their scores.
     *
     * @param gameCode  code of the game
     * @return          list of players with their scores
     */
    public List<PlayerScoreResponse> getScores(String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new IllegalArgumentException("Game " + gameCode + " not found"));

        return game.getPlayers().stream()
                .map(p -> new PlayerScoreResponse(p.getName(), p.getScore()))
                .sorted(Comparator.comparingInt(PlayerScoreResponse::score).reversed())
                .toList();
    }

    /**
     * Method to retrieve all Players in the Lobby of certain Game
     *
     * @param gameCode  code of the game
     * @return          list of players in the game lobby
     */
    public GameLobbyResponse getLobbyPlayers(String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new RuntimeException("Game " + gameCode + " not found"));

        List<String> playerNames = game.getPlayers()
                .stream()
                .map(Player::getName)
                .toList();

        return new GameLobbyResponse(playerNames);
    }

    /**
     * Method to retrieve Game state to reconnect Player to the ongoing Game.
     *
     * @param gameCode  the code of the game
     * @param playerId  player ID
     */
    public void getGameStateForPlayer(String gameCode, Long playerId) {
        Game game = gameRepository.findByGameCode(gameCode)
                .orElseThrow(() -> new IllegalArgumentException("Game " + gameCode + " not found"));
        Long topCardId = game.getCardIdList().get(game.getTopCardIndex());
        List<String> topCardSymbols = cardService.getCardSymbols(topCardId);

        Player player = playerService.getPlayerById(playerId);
        Long playerCardId = player.getCurrentCardId();
        List<String> playerCardSymbols = cardService.getCardSymbols(playerCardId);
        int score = player.getScore();

        socketNotifier.notifyGameReconnect(
                gameCode,
                player.getId(),
                topCardId,
                topCardSymbols,
                player.getCurrentCardId(),
                playerCardSymbols,
                score
        );
    }
}

