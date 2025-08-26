package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.Food;
import com.example.fooddeliveryproject.Entity.FoodRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FoodRatingRepository extends JpaRepository<FoodRating, UUID> {
    List<FoodRating> findByFood(Food food);

    @Query("SELECT fr FROM FoodRating fr WHERE fr.customer.id= :customerId AND fr.food.food_id = :foodId")
    Optional<FoodRating> findByCustomerAndFood(@Param("customerId") UUID customerId,
                                               @Param("foodId") UUID foodId);


//    List<FoodRating> findByFood_Food_id(UUID foodId);
//
//    // Get all ratings given by a specific customer
//    List<FoodRating> findByCustomer_Id(UUID customerId);
}
