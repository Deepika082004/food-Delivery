package com.example.fooddeliveryproject.Entity;

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
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID food_id;
    @Column(name="food_name")
    private String foodName;
    private Double foodPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    @JsonBackReference
    private Hotel hotel;


    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name="foodCustomer_id")
    @JsonManagedReference
    @JsonIgnore
    private FoodCustomer foodcustomer;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Delivery> deliveries;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return food_id != null && food_id.equals(food.food_id);
    }

    @Override
    public int hashCode() {
        return food_id != null ? food_id.hashCode() : 0;
    }

}
