package com.example.fooddeliveryproject.ResponseBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class                                                                                                                                                                       BillResponseBean {
    private UUID billId;

    private String customerName;
    private String customerPhone;
    private Double gst;
    private Double totalAmount;
    private UUID orderId;
    //private String orderStatus;
}
