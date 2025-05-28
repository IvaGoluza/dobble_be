package com.hr.fer.dobble.repository;

import com.hr.fer.dobble.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByGameCode(String gameCode);

}

