package com.example.RequestBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BillDto {

    private UUID id;
    private String customerName;
    private String customerPhone;
    private Double gst;
    private Double totalAmount;
}
