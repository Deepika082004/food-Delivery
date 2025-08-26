package com.example.fooddeliveryproject.repository;

import com.example.RequestBean.BillDto;
import com.example.fooddeliveryproject.Entity.Bill;
import com.example.fooddeliveryproject.ResponseBean.BillResponseBean;
import org.springframework.data.domain.Pageable;
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

//    @Query("SELECT new com.example.fooddeliveryproject.ResponseBean.BillResponseBean(" +
//            "b.Bill_id, b.CustomerName, b.CustomerPhone, b.gst, b.TotalAmount, " +
//            "b.finalAmount, b.order.id, b.paymentStatus, b.paymentMode, " +
//            "b.DiscountAmount, null)" +   // 👈 'orderedFoods' set as null for now
//            " FROM Bill b")
//    List<BillResponseBean> findAllBills(Pageable pageable);

}
