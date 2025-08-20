package com.example.fooddeliveryproject.controller;

import com.example.fooddeliveryproject.RequestBean.PaymentRequestBean;
import com.example.fooddeliveryproject.ResponseBean.PaymentResponseBean;
import com.example.fooddeliveryproject.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseBean> createPayment(@RequestBody PaymentRequestBean request) {
        PaymentResponseBean response = paymentService.createPayment(request);
        return ResponseEntity.ok(response);
    }

    // Get all payments
    @GetMapping
    public PageImpl<PaymentResponseBean> getAllPayments(Pageable pageable) {
        return paymentService.getAllPayments(pageable);
    }


    // Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseBean> getPaymentById(@PathVariable UUID id) {
        PaymentResponseBean response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(response);
    }

    // Update a payment
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseBean> updatePayment(
            @PathVariable UUID id,
            @RequestBody PaymentRequestBean request) {
        PaymentResponseBean response = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(response);
    }

    // Delete a payment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable UUID id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}
