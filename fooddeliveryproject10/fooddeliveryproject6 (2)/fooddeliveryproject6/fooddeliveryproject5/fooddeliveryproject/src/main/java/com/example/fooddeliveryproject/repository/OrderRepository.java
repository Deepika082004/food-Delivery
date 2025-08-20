package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.Bill;
import com.example.fooddeliveryproject.Entity.DeliverymanDetail;
import com.example.fooddeliveryproject.Entity.OrderFood;
import com.example.fooddeliveryproject.Enum.OrderStatus;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderFood, UUID> {
    Optional<OrderFood> findByBill(Bill bill);
    int countByDeliverymanDetailAndOrderStatusIn(DeliverymanDetail deliveryman, List<OrderStatus> statuses);
}
