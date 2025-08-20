package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.Delivery;
import com.example.fooddeliveryproject.Entity.DeliverymanDetail;
import com.example.fooddeliveryproject.Enum.DeliverymanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliverymanDetailsRepository extends JpaRepository<DeliverymanDetail, UUID> {
   // List<DeliverymanDetail> findByStatus(DeliverymanStatus deliverymanStatus);
//    List<DeliverymanDetail> findByAvailableTrue();
   List<DeliverymanDetail> findAllByAvailableTrue();
   Optional<DeliverymanDetail> findFirstByAvailableFalse();
    List<DeliverymanDetail> findAll();

    List<DeliverymanDetail> findByAvailableTrue();
    List<DeliverymanDetail> findByStatus(DeliverymanStatus status);
}

