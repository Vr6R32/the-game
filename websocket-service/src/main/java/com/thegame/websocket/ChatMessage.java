package com.thegame.websocket;

public record ChatMessage(String sender, String receiver, String payload) {
}
