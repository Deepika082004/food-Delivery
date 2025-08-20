package com.example.fooddeliveryproject.ResponseBean;

import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.example.fooddeliveryproject.Enum.PaymentMode;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class OrderResponseBean {
    private UUID orderId;
    private OrderStatus orderStatus;

    // Customer details
    private UUID customerId;
    private String customerName;
    private String customerPhone;

    // Hotel details
    private UUID hotelId;
    private String hotelName;
    private String hotelAddress;

    // Food details
    private List<String> foodNames;
    private Double totalPrice;
    private Double gst;
    private Double totalAmountWithGST;

    // Bill details
    private UUID billId;
    private PaymentMode paymentMode;
    private boolean paid;

    // Delivery man details
    private UUID deliveryManId;
    private String deliveryManName;
    private String deliveryManPhone;
}
