package com.example.fooddeliveryproject.ResponseBean;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class DeliveryRatingResponseBean {
    private UUID ratingId;
    private String rating;
    private String feedback;

    // deliveryman details
    private UUID deliverymanId;
    private String deliverymanName;
    private String deliverymanPhone;
}
