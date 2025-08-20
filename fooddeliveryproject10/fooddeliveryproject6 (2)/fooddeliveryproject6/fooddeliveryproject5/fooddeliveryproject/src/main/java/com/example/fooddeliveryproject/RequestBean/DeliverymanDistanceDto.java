package com.example.fooddeliveryproject.RequestBean;

import com.example.fooddeliveryproject.Entity.Location;
import jdk.jfr.DataAmount;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliverymanDistanceDto {
    private UUID id;
    private String name;
    private Location currentLocation;
    private double distanceKm;

}
