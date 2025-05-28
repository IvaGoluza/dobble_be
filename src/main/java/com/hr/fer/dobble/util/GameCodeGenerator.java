package com.hr.fer.dobble.util;

import com.hr.fer.dobble.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class GameCodeGenerator {

    private final GameRepository gameRepository;
    private final Random random = new SecureRandom();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Method to generate unique code for the game
     *
     * @return game code
     */
    public String generateUniqueGameCode() {
        int maxAttempts = 10;

        for (int i = 0; i < maxAttempts; i++) {
            String code = generateRandomCode();

            if (gameRepository.findByGameCode(code).isEmpty()) {
                return code;
            }
        }

        throw new IllegalStateException("Unable to generate a unique game code. Try again.");
    }

    /**
     * Method to generate random code
     * that consists od 6 alphanumeric digits
     *
     * @return  code
     */
    private String generateRandomCode() {
        int length = 6;
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        return code.toString();
    }
}
