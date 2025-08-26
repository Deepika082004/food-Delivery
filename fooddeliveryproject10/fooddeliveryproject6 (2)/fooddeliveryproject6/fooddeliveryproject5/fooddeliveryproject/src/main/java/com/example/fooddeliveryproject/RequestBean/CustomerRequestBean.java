package com.example.fooddeliveryproject.RequestBean;

import com.example.RequestBean.LocationDto;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestBean {

    @NotBlank(message="Name should not be null")
    private String customerName;
    @Pattern(regexp="\\d{10}", message="Phone number should have exactly 10 digits")
    private String mobile_no;
    @Email(message="provide valid Email")
    private String email;
    @Size(min=5,max=40,message="Address length should be betwwen 4 and 40")
    private String address;
    @Embedded
    private LocationCal locationCal;

}
