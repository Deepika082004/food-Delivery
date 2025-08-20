package com.example.fooddeliveryproject.ResponseBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRatingResponseBean {
    private UUID id;              // Rating ID
    private int rating;
    private String comment;

    // Extra response info
    private UUID hotelId;
    private String hotelName;

    private UUID customerId;
    private String customerName;
}
