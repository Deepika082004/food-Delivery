package com.example.fooddeliveryproject.Enum;

import java.util.Arrays;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    DELIVERED;
    public static OrderStatus fromString(String value) {
        return Arrays.stream(OrderStatus.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid OrderStatus: " + value));
    }
}
