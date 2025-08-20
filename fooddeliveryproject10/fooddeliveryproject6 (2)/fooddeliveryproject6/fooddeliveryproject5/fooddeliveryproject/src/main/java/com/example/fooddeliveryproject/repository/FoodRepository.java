package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.Food;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FoodRepository extends JpaRepository<Food, UUID> {
    // Find all foods for a given hotel
    List<Food> findByHotelId(UUID id);

    // Optional: filter by food name within a hotel
    List<Food> findByHotelIdAndFoodNameContainingIgnoreCase(UUID id, String foodName);

    List<Food> findByFoodNameInIgnoreCase(List<String> foodNames);

    List<Food> findByFoodNameInAndHotelId(@NotNull(message = "Food names cannot be empty") List<String> foodNames, UUID id);

    //List<Food> findByNameInAndHotelId(@NotNull(message = "Food names cannot be empty") List<String> foodNames, @NotNull(message = "Hotel ID cannot be null") UUID hotelId);
}
