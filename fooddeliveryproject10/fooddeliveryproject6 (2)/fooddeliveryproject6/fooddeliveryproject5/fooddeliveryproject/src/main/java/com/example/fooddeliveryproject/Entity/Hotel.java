package com.example.fooddeliveryproject.Entity;

import com.example.fooddeliveryproject.RequestBean.LocationCal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String hotel_name;
    private String hotel_phone;
    private String hotel_address;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderFood> orders;

    @OneToMany(mappedBy="hotel",fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Hotel_Rating> hotel_rating;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Food> food = new ArrayList<>();



//    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
//    @JoinColumn(name="foodcustomer",referencedColumnName ="id")
//    private FoodCustomer foodcustomer;
   @Embedded
   private LocationCal location;
    public LocationCal getLocation() {
        return location;
    }
    @OneToMany(mappedBy ="hotel",cascade=CascadeType.ALL)
    private List<Bill> bills;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return id != null && id.equals(hotel.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
