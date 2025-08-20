package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.FoodCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<FoodCustomer, UUID> {
}
