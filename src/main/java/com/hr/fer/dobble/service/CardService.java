package com.hr.fer.dobble.service;

import com.hr.fer.dobble.model.Card;
import com.hr.fer.dobble.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    /**
     * Method to generate Deck of shuffled cards as list of card IDs
     *
     * @return deck of cards
     */
    List<Long> generateCardDeck() {
        List<Long> deck = LongStream.range(1, 56)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(deck);

        return deck;
    }

    /**
     * Method to retrieve Symbols names of certain Card based on its ID.
     *
     * @param cardId    card ID
     * @return          List of symbols
     */
    List<String> getCardSymbols(Long cardId) {
        Card card = getCardById(cardId);
        return card.getSymbolList();
    }

    /**
     * Method to retrieve Card based on ID.
     *
     * @param cardId    card ID
     * @return          {@code Card} object
     */
    private Card getCardById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found. CardID: " + cardId));
    }
}
