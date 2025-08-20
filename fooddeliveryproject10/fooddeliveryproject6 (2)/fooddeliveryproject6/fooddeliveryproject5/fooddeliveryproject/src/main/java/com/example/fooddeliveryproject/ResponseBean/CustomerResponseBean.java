package com.example.fooddeliveryproject.ResponseBean;

import com.example.RequestBean.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseBean {
    private UUID id;
    private String customerName;
    private String mobile_no;
    private String email;
    private String address;
    private LocationDto location;
    private List<OrderResponseBean> orders;
}
