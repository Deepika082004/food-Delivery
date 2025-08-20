package com.example.RequestBean;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDto {
    private UUID id;
    private String hotel_name;
    @Pattern(regexp="\\d{10}",message="The phone should be exactly 10 digit")
    private String hotel_phone;
    private double overall_rating;

}
