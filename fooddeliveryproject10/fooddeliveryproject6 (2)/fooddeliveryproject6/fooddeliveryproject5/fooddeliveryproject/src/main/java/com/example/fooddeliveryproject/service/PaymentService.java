package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.Bill;
import com.example.fooddeliveryproject.Entity.Payment;
import com.example.fooddeliveryproject.RequestBean.PaymentRequestBean;
import com.example.fooddeliveryproject.ResponseBean.PaymentResponseBean;
import com.example.fooddeliveryproject.repository.BillRepository;
import com.example.fooddeliveryproject.repository.PaymentRepository;
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
public class PaymentService {
    private final OrderFoodService orderFoodService;
    private final DeliveryService deliveryService;
    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    private PaymentResponseBean toResponse(Payment payment) {
        return PaymentResponseBean.builder()
                .paymentId(payment.getId())
                .paymentMode(payment.getPaymentMode())
                .paymentStatus(payment.getPaymentStatus())
                .billId(payment.getBill() != null ? payment.getBill().getBill_id() : null)
                .totalAmount(payment.getBill() != null ? payment.getBill().getTotalAmount() : null)
//                .customerId(payment.getBill() != null && payment.getBill().getFoodcustomer() != null
//                        ? payment.getBill().getFoodcustomer().getId() : null)
//                .customerName(payment.getBill() != null && payment.getBill().getFoodcustomer() != null
//                        ? payment.getBill().getFoodcustomer().getCustomerName() : null)
                .build();
    }

    // Convert Request Bean → Entity
    private Payment toEntity(PaymentRequestBean request) {

        Payment payment = Payment.builder()
                .paymentMode(request.getPaymentMode())
                .paymentStatus("PAID")
                // default as per your earlier logic
                .build();

        if (request.getBillId() != null) {
            Bill bill = billRepository.findById(request.getBillId())
                    .orElseThrow(() -> new RuntimeException("Bill not found with id " + request.getBillId()));
            payment.setBill(bill);
        }

        return payment;
    }

    // Create Payment
    public PaymentResponseBean createPayment(PaymentRequestBean request) {
        Payment payment = toEntity(request);
        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    // Get All Payments
    public PageImpl<PaymentResponseBean> getAllPayments(Pageable pageable) {
        Page<Payment> pageResult = paymentRepository.findAll(pageable);

        List<PaymentResponseBean> paymentsList = pageResult.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(paymentsList, pageable, pageResult.getTotalElements());
    }


    // Get Payment by ID
    public PaymentResponseBean getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + id));
        return toResponse(payment);
    }

    // Update Payment
    public PaymentResponseBean updatePayment(UUID id, PaymentRequestBean request) {
        Payment existing = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + id));

        existing.setPaymentMode(request.getPaymentMode());
        existing.setPaymentStatus("PAID"); // or keep logic from request if needed

        if (request.getBillId() != null) {
            Bill bill = billRepository.findById(request.getBillId())
                    .orElseThrow(() -> new RuntimeException("Bill not found with id " + request.getBillId()));
            existing.setBill(bill);
        }

        Payment updated = paymentRepository.save(existing);
        return toResponse(updated);
    }

    // Delete Payment
    public void deletePayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + id));
        paymentRepository.delete(payment);
    }
}
