//package com.example.fooddeliveryproject.User;
//
//import com.example.fooddeliveryproject.RequestBean.LocationCal;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.Pattern;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class UserAccountRequestBean {
//    @NotEmpty(message="User should not be Empty provide valid user name")
//    private String username;
//    @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8}$", message = "the password size should be 8")
//    private String password;
//    @NotEmpty(message="Role should not be empty")
//    private String role;
//    private String customerName;
//    private String mobileNo;
//    private String email;
//    private String address;
//    private LocationCal locationCal;
//}
