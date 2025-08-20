package com.example.fooddeliveryproject.RequestBean;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class DeliverymanDetailsRequestBean {
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
    @Embedded
    private LocationCal location;
    private Boolean available;
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
