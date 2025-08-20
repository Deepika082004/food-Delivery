package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.DeliverymanRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliverymanRatingRepository extends JpaRepository<DeliverymanRating, UUID> {
    List<DeliverymanRating> findByRating(int rating);
}
