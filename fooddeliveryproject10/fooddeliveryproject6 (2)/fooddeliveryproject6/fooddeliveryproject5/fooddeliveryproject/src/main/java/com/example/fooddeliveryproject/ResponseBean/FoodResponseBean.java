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
public class FoodResponseBean {
    private UUID foodId;
    private String foodName;
    private Double foodPrice;

    // Hotel details (from Hotel mapping)
    private UUID hotelId;
    private String hotel_name;
    private String hotel_address;

    public FoodResponseBean(UUID foodId, String foodName, Double foodPrice) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
    }
}
