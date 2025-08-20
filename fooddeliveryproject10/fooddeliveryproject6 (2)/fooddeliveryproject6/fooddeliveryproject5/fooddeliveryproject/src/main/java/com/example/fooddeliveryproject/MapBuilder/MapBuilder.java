package com.example.fooddeliveryproject.MapBuilder;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {
    private final Map<String, Object> map = new HashMap<>();

    public static Map<String, Object> of(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
