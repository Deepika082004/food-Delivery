package com.example.fooddeliveryproject.RequestBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateRequest {
    private Double latitude;
    private Double longitude;
}
