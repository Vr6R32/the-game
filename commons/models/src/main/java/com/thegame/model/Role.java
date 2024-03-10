package com.thegame.model;

public enum Role {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER"),
    ROLE_AWAITING_DETAILS("AWAITING_DETAILS"),
    ROLE_MONITORING("MONITORING");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}