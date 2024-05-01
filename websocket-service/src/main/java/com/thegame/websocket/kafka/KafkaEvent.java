package com.thegame.websocket.kafka;


import com.thegame.model.Notification;

public record KafkaEvent(String receiverId, String destination, Notification notification) {
}
