package com.thegame.websocket.validator;

import com.thegame.websocket.filter.WebsocketUserPrincipal;

public class SubscriptionValidator {

    private SubscriptionValidator() {
    }

    public static boolean validateSubscription(String destination, WebsocketUserPrincipal user) {
        if (destination.startsWith("/user/") && destination.endsWith("/messages") || destination.endsWith("/errors")) {
            String[] parts = destination.split("/");
            Long subscriberUsername = Long.valueOf(parts[2].toLowerCase());
            return subscriberUsername.equals(user.userId());
        }
        return false;
    }
}
