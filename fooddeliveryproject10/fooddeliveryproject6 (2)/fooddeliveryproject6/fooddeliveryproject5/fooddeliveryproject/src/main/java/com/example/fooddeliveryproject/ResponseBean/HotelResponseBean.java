package com.example.fooddeliveryproject.ResponseBean;

import com.example.fooddeliveryproject.Entity.Hotel_Rating;
import com.example.fooddeliveryproject.RequestBean.LocationCal;
import com.fasterxml.jackson.annotation.JsonInclude;
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

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HotelResponseBean {
    private UUID id;
    private String hotel_name;
    private String hotel_phone;
    private String hotel_address;

    // Optional extra fields
    private double averageRating;
    private LocationCal locationCal;
    private double distance;

    // Example: return list of food names in this hotel
    private List<String> foodNames;

    public HotelResponseBean(UUID id, String hotelName, String hotelPhone, String hotelAddress, List<FoodResponseBean> list) {
        this.id = id;
        this.hotel_name = hotelName;
        this.hotel_phone = hotelPhone;
        this.hotel_address = hotelAddress;
    }


    public HotelResponseBean(UUID id, String hotelName, String hotelPhone, String hotelAddress, List<Hotel_Rating> hotelRating, Object o, List<FoodResponseBean> list) {
        this.id = id;
        this.hotel_name = hotelName;
        this.hotel_phone = hotelPhone;
        this.hotel_address = hotelAddress;
    }
}
