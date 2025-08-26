package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.Bill;
import com.example.fooddeliveryproject.Entity.OrderFood;
import com.example.fooddeliveryproject.Entity.Payment;
import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.example.fooddeliveryproject.Enum.PaymentMode;
import com.example.fooddeliveryproject.Enum.PaymentStatus;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.PaymentRequestBean;
import com.example.fooddeliveryproject.ResponseBean.BillResponseBean;
import com.example.fooddeliveryproject.ResponseBean.FoodItemInBillResponse;
import com.example.fooddeliveryproject.ResponseBean.PaymentResponseBean;
import com.example.fooddeliveryproject.repository.BillRepository;
import com.example.fooddeliveryproject.repository.OrderRepository;
import com.example.fooddeliveryproject.repository.PaymentRepository;
import jakarta.transaction.Transactional;
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
    private final OrderRepository orderRepository;

    private PaymentResponseBean toResponse(Payment payment) {
        return PaymentResponseBean.builder()
                .paymentId(payment.getId())
                //.orderId(payment.getOrder().getId())
                .paymentMode(payment.getPaymentMode())
                .paymentStatus(payment.getPaymentStatus())
                .bill(payment.getBill() != null ? toBillResponse(payment.getBill(), false) : null)
                // pass false → avoid infinite loop
                .build();

    }
    private BillResponseBean toBillResponse(Bill bill, boolean includePayment) {
        return BillResponseBean.builder()
                .billId(bill.getBill_id())
                .customerName(bill.getCustomerName())
                .customerPhone(bill.getCustomerPhone())
                .totalAmount(bill.getTotalAmount())
                .gst(bill.getGst())
                .discountAmount(bill.getDiscountAmount())
                .paymentMode(bill.getPaymentMode())
                //.payment(includePayment ? toResponse(bill.getPayment()) : null)
                // include payment only if requested
                .build();
        // Convert Request Bean → Entity
    }
    private Payment toEntity(PaymentRequestBean request) {

        Payment payment = Payment.builder()
                .paymentMode(request.getPaymentMode())
                //.paymentStatus("PAID")
                // default as per your earlier logic
                .build();

//        if (request.getBillId() != null) {
//            Bill bill = billRepository.findById(request.getBillId())
//                    .orElseThrow(() -> new RuntimeException("Bill not found with id " + request.getBillId()));
//            payment.setBill(bill);
//        }

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
                .orElseThrow(() -> new ConstraintValidationException("Error","Payment not found with id " + id));
        return toResponse(payment);
    }

    // Update Payment
    public PaymentResponseBean updatePayment(UUID id, PaymentRequestBean request) {
        Payment existing = paymentRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Payment not found with id " + id));

        existing.setPaymentMode(request.getPaymentMode());
//        existing.setPaymentStatus("PAID"); // or keep logic from request if needed
//
//        if (request.getBillId() != null) {
//            Bill bill = billRepository.findById(request.getBillId())
//                    .orElseThrow(() -> new RuntimeException("Bill not found with id " + request.getBillId()));
//            existing.setBill(bill);
//        }

        Payment updated = paymentRepository.save(existing);
        return toResponse(updated);
    }

    // Delete Payment
    public void deletePayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Payment not found with id " + id));
        paymentRepository.delete(payment);
    }
    @Transactional
    public PaymentResponseBean makePayment(UUID orderId, PaymentMode mode) {
        OrderFood order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Order not found"));

        // 🔹 Confirm order when payment starts
        order.setOrderStatus(OrderStatus.CONFIRMED);


        // 4️⃣ Create Payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMode(mode);
        payment.setPaymentStatus(mode == PaymentMode.COD ? PaymentStatus.UNPAID : PaymentStatus.PAID);

        // 5️⃣ Create Bill (only if payment done)
        Bill bill = new Bill();
        bill.setCustomerName(order.getCustomerName());
        bill.setPaid(payment.getPaymentStatus() == PaymentStatus.PAID);
        bill.setPaymentMode(payment.getPaymentMode() != null ? payment.getPaymentMode() : PaymentMode.COD);
        bill.setOrder(order);

        payment.setBill(bill);
        bill.setPayment(payment);

        paymentRepository.save(payment);

        // 6️⃣ Build response
        return PaymentResponseBean.builder()
                .paymentId(payment.getId())
                .paymentMode(payment.getPaymentMode())
                .paymentStatus(payment.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
               // .finalAmount(order.getFinalAmount())
                .bill(BillResponseBean.builder()
                        .billId(bill.getBill_id())
                        .customerName(bill.getCustomerName())
                        .totalAmount(bill.getTotalAmount())
                        .gst(bill.getGst())
                        .discountAmount(bill.getDiscountAmount())
                        .gst(bill.getGst())
                        .orderedFoods(order.getFoods().stream()
                                .map(f -> FoodItemInBillResponse.builder()
                                        .FoodName(f.getFoodName())
                                        .FoodPrice(f.getFoodPrice())
                                        .build())   // ✅ builder instead of new
                                .toList())
                        .build())
                .build();
        // ✅ closes PaymentResponseBean


}

    }

