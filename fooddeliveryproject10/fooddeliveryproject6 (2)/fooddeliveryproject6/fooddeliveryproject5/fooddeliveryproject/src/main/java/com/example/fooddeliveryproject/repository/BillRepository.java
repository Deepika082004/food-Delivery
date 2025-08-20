package com.example.fooddeliveryproject.repository;

import com.example.fooddeliveryproject.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {
    @Query("SELECT b FROM Bill b WHERE b.payment.paymentStatus = :paymentStatus")
    List<Bill> findByPaymentstatus(@Param("paymentmode") String paymentStatus);

}
