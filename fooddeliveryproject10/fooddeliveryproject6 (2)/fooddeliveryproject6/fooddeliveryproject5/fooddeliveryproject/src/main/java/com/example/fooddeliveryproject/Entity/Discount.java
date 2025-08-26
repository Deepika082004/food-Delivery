package com.example.fooddeliveryproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description; // e.g. "20% OFF on all pizzas"
    private Double discountPercent;
    private Double maxDiscountAmount; // e.g. max ₹100 off
    private Double minOrderValue; // e.g. valid for orders above ₹300
    private boolean active;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToMany
    @JoinTable(
            name = "food_discount",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    @JsonIgnore
    private List<Food> foods;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Bill> bills = new ArrayList<>();


    private java.time.LocalDate expiryDate;

}
