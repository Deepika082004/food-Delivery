package com.example.fooddeliveryproject.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel_Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int rating;
    private String comment;
    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @JsonManagedReference
    private Hotel hotel;

    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name="customer_id")
    private FoodCustomer customer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel_Rating hotel = (Hotel_Rating) o;
        return id != null && id.equals(hotel.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
