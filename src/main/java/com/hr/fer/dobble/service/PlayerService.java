package com.hr.fer.dobble.service;

import com.hr.fer.dobble.model.Game;
import com.hr.fer.dobble.model.Player;
import com.hr.fer.dobble.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    /**
     * Method to create a Player.
     *
     * @param name      name of the player
     * @param cardId    ID of the current Card that Player holds
     * @param game      Player Game
     * @return          {@code Player} object
     */
    Player createPlayer(String name, Long cardId, Game game) {
        Player player = Player.builder()
                .name(name)
                .score(0)
                .currentCardId(cardId)
                .game(game)
                .build();
        return savePlayer(player);
    }

    /**
     * Method to store Player.
     *
     * @param player Player object
     */
    Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    /**
     * Method to retrieve Players based on Game code.
     *
     * @param gameCode  code of the Game
     * @return          List of {@code Player} objects
     */
    List<Player> getPlayersByGameCode(String gameCode) {
        return playerRepository.findByGame_GameCode(gameCode);
    }

    /**
     * Method to retrieve Player based on ID.
     *
     * @param playerId  player ID
     * @return          {@code Player} object
     */
    Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player with id " + playerId + " not found"));
    }
}

