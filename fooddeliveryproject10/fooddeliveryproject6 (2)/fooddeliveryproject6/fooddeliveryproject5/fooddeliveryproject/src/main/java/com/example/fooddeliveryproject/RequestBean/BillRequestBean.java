package com.example.fooddeliveryproject.RequestBean;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillRequestBean {

        private UUID billId;

        @NotBlank(message = "Customer name is required")
        private String customerName;

        @Pattern(regexp = "^[1-9]\\d{9}$", message = "Invalid phone number")
        private String customerPhone;

        @PositiveOrZero(message = "GST must be zero or positive")
        private Double gst;

        @Positive(message = "Total amount must be greater than zero")
        private Double totalAmount;

        @NotNull(message = "Order ID is required")
        private UUID orderId;
    }



