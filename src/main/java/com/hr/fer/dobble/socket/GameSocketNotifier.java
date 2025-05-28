package com.hr.fer.dobble.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GameSocketNotifier {

    private final SimpMessagingTemplate messaging;

    /**
     * Method to notify players in the Game lobby that another Player joined the Game.
     *
     * @param gameCode      code of the game
     * @param playerNames   players
     */
    public void notifyLobbyPlayers(String gameCode, List<String> playerNames) {
        messaging.convertAndSend(
                "/topic/game/" + gameCode + "/lobby",
                Map.of("players", playerNames)
        );
    }

    /**
     * Method to notify Player of the Game Start and provide Player current Card and Top Card of the Game
     *
     * @param gameCode              code of the game
     * @param playerId              player ID
     * @param topCardId             starting top card of the deck ID
     * @param topCardSymbols        8 symbols on the top card of the deck
     * @param playerCardId          ID of the card that player holds in the game start
     * @param playerCardSymbols     8 symbols on the player's card
     */
    public void notifyGameStart(
            String gameCode,
            Long playerId,
            Long topCardId,
            List<String> topCardSymbols,
            Long playerCardId,
            List<String> playerCardSymbols
    ) {
        notifyGameReconnect(
                gameCode,
                playerId,
                topCardId,
                topCardSymbols,
                playerCardId,
                playerCardSymbols,
                0
        );
    }

    /**
     * Method to reconnect the PLayer to the Game and provide Player current Card and Top Card of the Game
     * as well as player score.
     *
     * @param gameCode              code of the game
     * @param playerId              player ID
     * @param topCardId             starting top card of the deck ID
     * @param topCardSymbols        8 symbols on the top card of the deck
     * @param playerCardId          ID of the card that player holds in the game start
     * @param playerCardSymbols     8 symbols on the player's card
     * @param playerScore           player score
     */
    public void notifyGameReconnect(
            String gameCode,
            Long playerId,
            Long topCardId,
            List<String> topCardSymbols,
            Long playerCardId,
            List<String> playerCardSymbols,
            int playerScore
    ) {
        messaging.convertAndSend(
                "/topic/game/" + gameCode + "/player/" + playerId,
                Map.of(
                        "type", "reconnect_state",
                        "topCardId", topCardId,
                        "topCardSymbols", topCardSymbols,
                        "playerCardId", playerCardId,
                        "playerCardSymbols", playerCardSymbols,
                        "playerScore", playerScore
                )
        );
    }

    /**
     * Method to notify Players that one Player has taken a Top Card from the Deck.
     *
     * @param gameCode              code of the game
     * @param winnerName            name of the player that took the top card
     * @param newTopCardId          ID of the new top card of the deck
     * @param newTopCardSymbols     8 symbols on the top card
     * @param prevTopCardId         ID of the previous top card of the deck
     * @param prevTopCardSymbols    8 symbols on the previous top card
     */
    public void notifyCardWon(
            String gameCode,
            String winnerName,
            Long newTopCardId,
            List<String> newTopCardSymbols,
            Long prevTopCardId,
            List<String> prevTopCardSymbols
    ) {
        messaging.convertAndSend(
                "/topic/game/" + gameCode,
                Map.of(
                        "type", "card_won",
                        "topCardId", newTopCardId,
                        "topCardSymbols", newTopCardSymbols,
                        "prevTopCardId", prevTopCardId,
                        "prevTopCardSymbols", prevTopCardSymbols,
                        "winnerName", winnerName
                )
        );
    }

    /**
     * Method to notify Game End.
     *
     * @param gameCode  code of the game
     */
    public void notifyGameOver(String gameCode) {
        messaging.convertAndSend(
                "/topic/game/" + gameCode,
                Map.of(
                        "type", "game_over",
                        "message", "Game has ended. All cards have been played."
                )
        );
    }

}

