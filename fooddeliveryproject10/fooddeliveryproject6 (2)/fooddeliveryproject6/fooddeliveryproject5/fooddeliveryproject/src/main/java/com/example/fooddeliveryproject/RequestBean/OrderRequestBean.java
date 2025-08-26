package com.example.fooddeliveryproject.RequestBean;

import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.example.fooddeliveryproject.Enum.PaymentMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestBean {
    @NotNull(message = "Customer ID cannot be null")
    private UUID customerId;   // Customer who places the order

    @NotNull(message = "Hotel ID cannot be null")
    private UUID hotelId;      // Hotel where the order is placed

    @NotNull(message = "Food names cannot be empty")
    private List<String> foodNames; // Foods chosen from the hotel
    private PaymentMode paymentMode;
    private List<OrderItemRequest> orderItems;
}
