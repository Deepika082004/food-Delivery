package com.example.RequestBean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryDto {
    private UUID delivery_id;
    @NotBlank(message="name should not be empty")
    private String customer_name;
    @Pattern(regexp="\\d{10}",message="The phone number should be exactly 10 digit")
    private String mobile_phone;

    private String food_name;
    private String email;
    private String address;
}
