package com.example.fooddeliveryproject.ResponseBean;

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
public class DeliverymanResponseBean {
    private UUID id;
    private String deliveryman_name;
    private String deliveryman_phone;
    private String deliveryman_email;
    private String deliveryman_address;
    private String Licence_no;
    private Boolean available;

}
