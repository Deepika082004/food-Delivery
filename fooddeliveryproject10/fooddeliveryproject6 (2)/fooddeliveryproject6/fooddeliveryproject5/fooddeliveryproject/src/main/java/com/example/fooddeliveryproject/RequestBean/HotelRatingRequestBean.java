package com.example.fooddeliveryproject.RequestBean;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelRatingRequestBean {
    @NotNull(message = "Hotel ID cannot be null")
    private UUID hotelId;

    @NotNull(message = "Customer ID cannot be null")
    private UUID customerId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private int rating;

    @NotBlank(message = "Comment cannot be empty")
    private String comment;
}
