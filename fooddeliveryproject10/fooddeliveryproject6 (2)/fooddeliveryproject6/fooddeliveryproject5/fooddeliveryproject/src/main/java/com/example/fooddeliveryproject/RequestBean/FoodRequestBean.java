package com.example.fooddeliveryproject.RequestBean;

import com.example.fooddeliveryproject.Enum.CuisineType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodRequestBean {
    @NotBlank(message = "Food name should not be blank")
    private String foodName;

    @NotNull(message = "Food price must be provided")
    @Min(value = 1, message = "Food price must be at least 1")
    private Double foodPrice;

    @NotNull(message = "Hotel ID must be provided")
    private UUID hotelId;

    private String category;       // "Pizza", "Biryani", "Chinese"
    private Boolean bestseller;    // true/false
    private String imageUrl;       // food photo
    private Double discountPercent;
    private String offerDescription;
    private CuisineType cuisineType;

}
