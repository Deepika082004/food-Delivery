package com.example.fooddeliveryproject.RequestBean;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderByFoodName {
    private List<String> foodNames;
    private String orderStatus;
}
