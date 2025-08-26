package com.example.fooddeliveryproject.RequestBean;

import com.example.fooddeliveryproject.Enum.PaymentMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestBean {

    private UUID orderId;
    @NotBlank(message="payMode should not be null")
    private PaymentMode paymentMode;
}
