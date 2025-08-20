package com.example.fooddeliveryproject.RequestBean;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelRequestBean {
    @NotBlank(message = "Hotel name should not be blank")
    private String hotel_name;

    @Pattern(regexp = "\\d{10}", message = "The phone number should be exactly 10 digits")
    private String hotel_phone;

    @NotBlank(message = "Hotel address should not be blank")
    private String hotel_address;
    @Embedded
    private LocationCal location;
}
