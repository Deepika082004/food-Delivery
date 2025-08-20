package com.example.fooddeliveryproject.ResponseBean;

import com.example.fooddeliveryproject.Enum.PaymentMode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseBean {
    private UUID paymentId;
    private PaymentMode paymentMode;
    private String paymentStatus;

    // Bill details (from Bill mapping)
    private UUID billId;
    private Double totalAmount;
}
