package com.example.fooddeliveryproject.RequestBean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryRequestBean {
    @NotNull(message = "Customer Id is required")
    private UUID customerId;   // FoodCustomer reference

    @NotNull(message = "Food Id is required")
    private UUID foodId;       // Food reference

    private UUID deliverymanDetailId;


}
