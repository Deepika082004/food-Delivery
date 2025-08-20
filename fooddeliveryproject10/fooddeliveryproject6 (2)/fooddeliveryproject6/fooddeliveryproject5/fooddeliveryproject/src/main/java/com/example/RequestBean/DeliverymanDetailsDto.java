package com.example.RequestBean;

import jakarta.validation.constraints.Email;
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
public class DeliverymanDetailsDto {
    private UUID id;
    @NotBlank(message="Deliveryman name should not be blank")
    private String deliveryman_name;
    @Pattern(regexp="\\d{10}", message="Provide mobile number exactly 10 digits")
    private String deliveryman_phone;
    @Email(message="provide valid email")
    private String deliveryman_email;
    private String deliveryman_address;
    @NotBlank(message="Licence sholud not be blank")
    private String Licence_no;
}
