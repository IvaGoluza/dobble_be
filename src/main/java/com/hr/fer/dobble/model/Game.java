package com.hr.fer.dobble.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_code", unique = true, nullable = false)
    private String gameCode;

    /**
     * A player that created the game
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id")
    private Player creator;

    /**
     * A random sequence of IDs representing a shuffled cards deck.
     * Dobble Deck has 55 cards - IDs [1, 55]
     * e.g. 1#2#4#5...
     *
     */
    @Column(name = "cards", nullable = false)
    private String cards;

    /**
     * A currently active card index
     * topCardId = cards.get(topCardIndex)
     */
    @Column(name = "topCardIndex")
    private int topCardIndex;

    @Column(name = "started", nullable = false)
    private boolean started = false;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    /**
     * Utility method to get cards as List<Integer>
     *
     * @return  List of cards
     */
    public List<Long> getCardIdList() {
        if (cards == null || cards.isBlank()) return List.of();
        return Arrays.stream(cards.split("#"))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    /**
     * Utility method to save shuffled cards deck in the right format
     * e.g. 1#2#4#5...
     *
     * @param ids   Ids of the cards contained in the deck
     */
    public void setCardIdList(List<Long> ids) {
        this.cards = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("#"));
    }

}
