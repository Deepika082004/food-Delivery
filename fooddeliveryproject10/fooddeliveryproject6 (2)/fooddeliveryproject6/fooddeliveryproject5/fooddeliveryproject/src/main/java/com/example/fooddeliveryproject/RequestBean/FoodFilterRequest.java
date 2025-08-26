package com.example.fooddeliveryproject.RequestBean;

import com.example.fooddeliveryproject.Enum.CuisineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodFilterRequest {
    private String foodName;
    private Double minPrice;
    private Double maxPrice;
    private String category;
    private Boolean bestseller;
    private CuisineType cuisineType;
    private Double minDiscountPercent;
    private Double maxDiscountPercent;
    private UUID hotelId;
    private String hotelName;
    private String hotelAddress;
}
