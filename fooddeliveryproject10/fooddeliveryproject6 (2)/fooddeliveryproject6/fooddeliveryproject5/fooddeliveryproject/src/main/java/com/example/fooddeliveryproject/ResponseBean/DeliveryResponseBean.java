package com.example.fooddeliveryproject.ResponseBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryResponseBean {
    private UUID deliveryId;

    // Customer details (from FoodCustomer mapping)
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    // Food details (from Food mapping)
    private UUID foodId;
    private String foodName;
    private Double foodPrice;

    // Deliveryman details (from DeliverymanDetail mapping)
    private UUID deliverymanDetailId;
    private String deliverymanName;
    private String deliverymanPhone;
}

