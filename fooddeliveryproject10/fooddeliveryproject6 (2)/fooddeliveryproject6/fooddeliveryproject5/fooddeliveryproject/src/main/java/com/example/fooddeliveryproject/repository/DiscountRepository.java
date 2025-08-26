package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    @Query("SELECT d FROM Discount d WHERE d.hotel.id = :hotelId AND d.active = true")
    Optional<Discount> findActiveDiscountForHotel(@Param("hotelId") UUID hotelId);

}
