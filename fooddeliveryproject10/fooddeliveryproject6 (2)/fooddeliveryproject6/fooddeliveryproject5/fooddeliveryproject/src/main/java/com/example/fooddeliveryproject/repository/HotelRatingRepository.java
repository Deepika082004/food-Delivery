package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.Hotel;
import com.example.fooddeliveryproject.Entity.Hotel_Rating;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HotelRatingRepository extends JpaRepository<Hotel_Rating, UUID> {
//    @Query("SELECT h FROM Hotel h WHERE h.hotel_rating.rating >= :rating")
//    List<Hotel> findByRatingGreaterThanEqual(@Param("rating")double rating);
    List<Hotel_Rating> findByHotelId(UUID hotelId);
    @Query("SELECT AVG(r.rating) FROM Hotel_Rating r WHERE r.hotel.id = :hotelId")
    Double getAverageRatingByHotelId(@Param("hotelId") UUID hotelId);
}
