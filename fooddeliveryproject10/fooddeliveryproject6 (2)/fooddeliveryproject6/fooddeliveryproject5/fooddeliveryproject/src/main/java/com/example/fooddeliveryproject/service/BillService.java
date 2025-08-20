package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.*;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.BillRequestBean;
import com.example.fooddeliveryproject.ResponseBean.BillResponseBean;
import com.example.fooddeliveryproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class BillService {
    private final BillRepository billRepository;
    private final CustomerRepository foodCustomerRepository;
    private final HotelRepository hotelRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderFoodRepository;

    // Create Bill
    public BillResponseBean createBill(BillRequestBean request) {
        // Fetch order to get order status
        OrderFood order = orderFoodRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ConstraintValidationException("error","Order not found"));
        Bill bill = Bill.builder()
                .Bill_id(request.getBillId())
                .CustomerName(request.getCustomerName())
                .CustomerPhone(request.getCustomerPhone())
                .gst(request.getGst())
                .TotalAmount(request.getTotalAmount())
                .order(order)
                .build();

        Bill savedBill = billRepository.save(bill);

        return mapToResponseBean(savedBill);
    }

    // Get all bills
    public PageImpl<BillResponseBean> getAllBills(Pageable pageable) {
        Page<Bill> pageResult = billRepository.findAll(pageable); // Fetch paginated data from DB

        List<BillResponseBean> billList = pageResult.getContent().stream()
                .map(this::mapToResponseBean) // Convert each entity to DTO
                .collect(Collectors.toList());

        return new PageImpl<>(billList, pageable, pageResult.getTotalElements());
    }

    // Get bill by ID
    public BillResponseBean getBillById(UUID id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Bill not found"));
        return mapToResponseBean(bill);
    }

    // Update bill
    public BillResponseBean updateBill(UUID id, BillRequestBean request) {
        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Bill not found"));

        OrderFood order = orderFoodRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Order not found"));

        existingBill.setCustomerName(request.getCustomerName());
        existingBill.setCustomerPhone(request.getCustomerPhone());
        existingBill.setGst(request.getGst());
        existingBill.setTotalAmount(request.getTotalAmount());
        existingBill.setOrder(order);

        Bill updatedBill = billRepository.save(existingBill);

        return mapToResponseBean(updatedBill);
    }

    // Delete bill
    public void deleteBill(UUID id) {
        if (!billRepository.existsById(id)) {
            throw new ConstraintValidationException("Error","Bill not found");
        }
        billRepository.deleteById(id);
    }

    // Mapper
    private BillResponseBean mapToResponseBean(Bill bill) {
        return BillResponseBean.builder()
                .billId(bill.getBill_id())
                .customerName(bill.getCustomerName())
                .customerPhone(bill.getCustomerPhone())
                .gst(bill.getGst())
                .totalAmount(bill.getTotalAmount())
                .orderId(bill.getOrder().getId())
                // assuming status field in Order
                .build();
    }
}
