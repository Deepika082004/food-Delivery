package com.example.RequestBean;

import com.example.fooddeliveryproject.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDto {
    private UUID id;
    private UUID food_id;
    private OrderStatus orderStatus;
    private String customerName;
    private Double total_price;
    private List<FoodDto>foods;
//    private Double deliverymanDistance;
//    private UUID customerId;       // required to identify customer
//    private UUID hotelId;          // required to identify hotel
//
//    // New response fields (server fills these)
//    private UUID deliverymanId;
//    private String deliverymanName;
//    private String hotelName;
}
