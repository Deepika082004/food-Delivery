package com.example.fooddeliveryproject.RequestBean;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemRequest {
    private UUID foodId;
    private int quantity;

}
