package com.hr.fer.dobble.dto;

import com.hr.fer.dobble.model.Card;

public record ReconnectResponse (Card topCard, Card playerCard, int playerScore) {}
