package com.example.fooddeliveryproject.ResponseBean;

import com.example.fooddeliveryproject.Entity.Food;
import com.example.fooddeliveryproject.Entity.OrderFood;
import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.example.fooddeliveryproject.Enum.PaymentMode;
import com.example.fooddeliveryproject.RequestBean.LocationCal;
import com.example.fooddeliveryproject.RequestBean.OrderItemRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

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

    // Delivery man details
    private UUID deliveryManId;
    private String deliveryManName;
    private String deliveryManPhone;
    private List<OrderItemRequest> items;
    private List<FoodResponseBean> foods;
    private BillResponseBean bill;
    private PaymentResponseBean payment;
    private DeliverymanResponseBean deliveryman;
    private LocationCal locationCal;
}
