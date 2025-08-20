package com.example.fooddeliveryproject.RequestBean;

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
}
