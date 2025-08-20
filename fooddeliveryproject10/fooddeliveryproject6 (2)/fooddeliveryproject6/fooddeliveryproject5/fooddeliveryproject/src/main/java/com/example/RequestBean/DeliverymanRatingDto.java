package com.example.RequestBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliverymanRatingDto {
    private UUID id;
    private String rating;
    private String feedback;
}
