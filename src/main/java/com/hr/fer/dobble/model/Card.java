package com.hr.fer.dobble.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 8 Symbols of a Dobble card
     * e.g. cactus#apple#bolt#car#pencil#carrot#heart#skull
     *
     */
    @Column(nullable = false, unique = true)
    private String symbols;

    /**
     * Utility method to get card symbols as List<String>
     *
     * @return List of symbols
     */
    public List<String> getSymbolList() {
        if (symbols == null || symbols.isBlank()) return List.of();

        return List.of(symbols.split("#"));
    }

}
