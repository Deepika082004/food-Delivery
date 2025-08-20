package com.example.fooddeliveryproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="customer_name",unique = true)
    private String customerName;
    private String mobile_no;
    private String email;
    private String address;




    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customer_location")
    private Location location;

    @OneToMany(mappedBy ="foodcustomer",cascade=CascadeType.ALL)
    @JsonBackReference
    private List<OrderFood> orders;

    @OneToMany(mappedBy ="foodcustomer",cascade=CascadeType.ALL)
    @JsonBackReference
    private List<Delivery> deliveries;

//    @OneToMany(mappedBy = "foodcustomer", cascade = CascadeType.ALL)
//    @JsonBackReference
//    private List<Hotel> hotels=new ArrayList<>();

    @OneToMany(mappedBy = "foodcustomer", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<DeliverymanDetail> deliverymans=new ArrayList<>();

    @OneToMany(mappedBy = "foodcustomer", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Food> foods=new ArrayList<>();

    @OneToOne(mappedBy = "foodcustomer")
    @JsonBackReference
    private Bill bill;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Hotel_Rating> hotelRating;


    public void setOrder(List<OrderFood> orderList) {
        this.orders = orderList;
    }
//
//    public void setHotel(List<Hotel> hotelList) {
//        this.hotels = hotelList;
//    }
//
    public void setFood(List<Food> foodList) {
        this.foods = foodList;
    }
    public void setDeliverymans(List<DeliverymanDetail> deliverymanList) {
        this.deliverymans = deliverymanList;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodCustomer customer = (FoodCustomer) o;
        return id != null && id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
