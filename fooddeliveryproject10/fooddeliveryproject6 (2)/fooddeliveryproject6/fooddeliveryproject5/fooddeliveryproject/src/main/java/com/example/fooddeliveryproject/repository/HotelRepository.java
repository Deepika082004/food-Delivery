package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID> {

}
