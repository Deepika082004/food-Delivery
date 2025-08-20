package com.example.RequestBean;

import com.example.fooddeliveryproject.Enum.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {
    private UUID billId;
    private PaymentMode paymentMode;
    private String paymentStatus;
    private Double totalAmount;
    private Double gst;

    private String customerName;
    private String customerPhone;

    private UUID orderId;
    private String orderStatus;
    private Double orderTotalPrice;

    private String deliveryManName; // optional if assigned
    private UUID deliveryManId;
}
