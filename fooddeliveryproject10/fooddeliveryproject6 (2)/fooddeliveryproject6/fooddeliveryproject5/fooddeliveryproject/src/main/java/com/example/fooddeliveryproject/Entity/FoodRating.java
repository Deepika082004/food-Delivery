package com.example.fooddeliveryproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FoodRating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "customer_id")
    private FoodCustomer customer;

    private Double rating;

    private String review;

    private LocalDateTime ratedAt;
}
