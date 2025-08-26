package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.*;
import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.example.fooddeliveryproject.Enum.PaymentMode;
import com.example.fooddeliveryproject.Enum.PaymentStatus;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.BillRequestBean;
import com.example.fooddeliveryproject.ResponseBean.BillResponseBean;
import com.example.fooddeliveryproject.ResponseBean.FoodItemInBillResponse;
import com.example.fooddeliveryproject.ResponseBean.FoodResponseBean;
import com.example.fooddeliveryproject.repository.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final CouponRepository couponRepository;
    private final DiscountRepository discountRepository;
    @PersistenceContext
    private EntityManager entityManager;

    // Create Bill
    public BillResponseBean createBill(BillRequestBean request) {
        // Fetch order to get order status
        OrderFood order = orderFoodRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ConstraintValidationException("error", "Order not found"));
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
    public PageImpl<BillResponseBean> findAllBills(Pageable pageable) {
        Page<Bill> pageResult = billRepository.findAll(pageable); // Fetch paginated data from DB

        List<BillResponseBean> billList = pageResult.getContent().stream()
                .map(this::mapToResponseBean) // Convert each entity to DTO
                .collect(Collectors.toList());

        return new PageImpl<>(billList, pageable, pageResult.getTotalElements());
    }

    // Get bill by ID
    public BillResponseBean getBillById(UUID id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error", "Bill not found"));
        return mapToResponseBean(bill);
    }

    // Update bill
    public BillResponseBean updateBill(UUID id, BillRequestBean request) {
        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error", "Bill not found"));

        OrderFood order = orderFoodRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ConstraintValidationException("Error", "Order not found"));

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
            throw new ConstraintValidationException("Error", "Bill not found");
        }
        billRepository.deleteById(id);
    }

    @Transactional
    public BillResponseBean generateBillForOrder(UUID orderId) {
        OrderFood order = orderFoodRepository.findById(orderId)
                .orElseThrow(() -> new ConstraintValidationException("Error", "Order not found"));

        if (order.getOrderStatus() != OrderStatus.CONFIRMED &&
                order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new ConstraintValidationException("Error", "Bill can only be generated for confirmed or delivered orders");
        }

        FoodCustomer customer = order.getFoodcustomer();
        List<Food> orderedFoods = order.getFoods();

        double totalAmount = orderedFoods.stream()
                .mapToDouble(Food::getFoodPrice)
                .sum();
        // Apply discount if present in order
        double discount = order.getDiscountAmount(); // will be 0 if not set
        double finalAmount = order.getFinalAmount() > 0 ? order.getFinalAmount() : (totalAmount - discount);


        double gst = finalAmount * 0.05; // GST on final amount after discount
        double totalAmountWithGst = finalAmount + gst;

        Bill bill = Bill.builder()
                .CustomerName(customer != null ? customer.getCustomerName() : "Unknown")
                .CustomerPhone(customer != null ? customer.getMobile_no() : "Unknown")
                .TotalAmount(finalAmount)         // discounted amount
                .gst(gst)
                .paid(order.getPayment() != null && order.getPayment().getPaymentStatus() == PaymentStatus.PAID)
                .paymentMode(order.getPayment() != null ? order.getPayment().getPaymentMode() : null)
                .order(order)
                .foodcustomer(customer)
                .foodDetails(orderedFoods.stream()
                        .map(f -> f.getFoodName() + " - " + f.getFoodPrice())
                        .toList())
                .DiscountAmount(discount)  // store discount in bill
                .build();

        if (order.getPayment() != null) {
            order.getPayment().setBill(bill);
            bill.setPayment(order.getPayment());
        }

        order.setBill(bill);
        billRepository.save(bill);

        return mapToResponseBean(bill);
    }


    // Helper method
    private BillResponseBean mapToResponseBean(Bill bill) {
        String customerPhone = bill.getCustomerPhone();
        if (customerPhone == null && bill.getCustomerPhone() != null) {
            customerPhone = bill.getCustomerPhone();
        }

        PaymentStatus paymentStatus = bill.isPaid() ? PaymentStatus.PAID : PaymentStatus.UNPAID;

        PaymentMode paymentMode = bill.getPaymentMode();
        if (paymentMode == null && bill.getPayment() != null) {
            paymentMode = bill.getPayment().getPaymentMode();
        }

        // Map order items with quantity and total price
        List<FoodItemInBillResponse> foods = new ArrayList<>();
        if (bill.getOrder() != null && bill.getOrder().getOrderItems() != null) {
            foods = bill.getOrder().getOrderItems().stream()
                    .map(item -> FoodItemInBillResponse.builder()
                            .FoodName(item.getFood().getFoodName())
                            .FoodPrice(item.getFood().getFoodPrice())
                            .quantity(item.getQuantity())
                            .totalPrice(item.getFood().getFoodPrice() * item.getQuantity())
                            .build())
                    .toList();
        }

        return BillResponseBean.builder()
                .billId(bill.getBill_id())
                .customerName(bill.getCustomerName())
                .customerPhone(bill.getCustomerPhone())
                .gst(bill.getGst())
                .totalAmount(bill.getFinalAmount()) // finalAmount after discounts + GST
                .subTotal(bill.getSubTotal())
                .discountAmount(bill.getDiscountAmount())
                .finalAmount(bill.getFinalAmount())
                .paymentStatus(bill.getPaymentStatus())
                .paymentMode(bill.getPaymentMode())
                .orderedFoods(foods)
                .build();
    }



    @Transactional
    public List<BillResponseBean> filterBills(
            String customerName,
            String customerPhone,
            String paymentStatus, // "PAID" or "UNPAID"
            String paymentMode,   // "COD", "GPAY", etc.
            String hotelName,
            UUID orderId
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Bill> cq = cb.createQuery(Bill.class);
        Root<Bill> billRoot = cq.from(Bill.class);

        List<Predicate> predicates = new ArrayList<>();

        // Join with FoodCustomer if needed
        Join<Bill, FoodCustomer> customerJoin = billRoot.join("foodcustomer", JoinType.LEFT);
        if (customerName != null && !customerName.isEmpty()) {
            predicates.add(cb.like(cb.lower(customerJoin.get("customerName")), "%" + customerName.toLowerCase() + "%"));
        }
        if (customerPhone != null && !customerPhone.isEmpty()) {
            predicates.add(cb.like(customerJoin.get("mobile_no"), "%" + customerPhone + "%"));
        }

        // Join with Payment if needed
        Join<Bill, Payment> paymentJoin = billRoot.join("payment", JoinType.LEFT);
        if (paymentStatus != null && !paymentStatus.isEmpty()) {
            boolean paid = paymentStatus.equalsIgnoreCase("PAID");
            predicates.add(cb.equal(billRoot.get("paid"), paid));
        }
        if (paymentMode != null && !paymentMode.isEmpty()) {
            predicates.add(cb.equal(billRoot.get("paymentMode"), PaymentMode.valueOf(paymentMode)));
        }

        // Join with Hotel if needed
        Join<Bill, Hotel> hotelJoin = billRoot.join("hotel", JoinType.LEFT);
        if (hotelName != null && !hotelName.isEmpty()) {
            predicates.add(cb.like(cb.lower(hotelJoin.get("hotel_name")), "%" + hotelName.toLowerCase() + "%"));
        }

        // Filter by orderId if provided
        if (orderId != null) {
            predicates.add(cb.equal(billRoot.join("order", JoinType.LEFT).get("id"), orderId));
        }

        cq.select(billRoot).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Bill> query = entityManager.createQuery(cq);
        List<Bill> bills = query.getResultList();

        // Map to response bean
        List<BillResponseBean> response = new ArrayList<>();
        for (Bill bill : bills) {
            response.add(mapToResponseBean(bill));
        }
        return response;
    }

    public Bill generateBill(UUID orderId, String couponCode) {
        return null;
    }

    @Transactional
    public Bill generateBillWithCoupen(UUID orderId, String couponCode) {
        OrderFood order = orderFoodRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Get existing bill or create new if not present
        Bill bill = order.getBill();
        if (bill == null) {
            bill = Bill.builder()
                    .foodcustomer(order.getFoodcustomer())
                    .hotel(order.getHotel())
                    .order(order)
                    .build();
        }

//        double subTotal = order.getFoods().stream()
//                .mapToDouble(Food::getFoodPrice)
//                .sum();
        double subTotal = order.getOrderItems().stream()
                .mapToDouble(item -> item.getFood().getFoodPrice() * item.getQuantity())
                .sum();

        double discountAmount = 0.0;
        double couponAmount = 0.0;

        // Apply hotel discount
        Discount discount = null;
        if (order.getHotel() != null) {
            discount = discountRepository.findActiveDiscountForHotel(order.getHotel().getId())
                    .orElse(null);
        }

        if (discount != null && discount.isActive() &&
                subTotal >= discount.getMinOrderValue() &&
                discount.getExpiryDate().isAfter(LocalDate.now())) {

            discountAmount = (subTotal * discount.getDiscountPercent() / 100);
            discountAmount = Math.min(discountAmount, discount.getMaxDiscountAmount());
        }

        double afterDiscount = subTotal - discountAmount;

        // Apply coupon
        Coupon coupon = null;
        if (couponCode != null) {
            coupon = couponRepository.findByCode(couponCode)
                    .orElseThrow(() -> new RuntimeException("Invalid coupon"));

            if (coupon.isActive() &&
                    afterDiscount >= coupon.getMinOrderValue()){

                couponAmount = (afterDiscount * coupon.getDiscountPercent() / 100);
                couponAmount = Math.min(couponAmount, coupon.getMaxDiscountAmount());
            }
        }

        double afterDiscount1 = subTotal - discountAmount;
        double afterCoupon = afterDiscount1 - couponAmount;
        double gst = afterCoupon * 0.05;
        double finalAmount = afterCoupon + gst;

        // Update bill fields
        bill.setSubTotal(subTotal);
        bill.setDiscountAmount(discountAmount + couponAmount);
        bill.setFinalAmount(finalAmount);
        bill.setGst(gst);
        bill.setPaid(order.getPayment() != null && order.getPayment().getPaymentStatus() == PaymentStatus.PAID);
        bill.setPaymentMode(order.getPayment() != null ? order.getPayment().getPaymentMode() : null);
        bill.setPaymentStatus(order.getPayment() != null ? order.getPayment().getPaymentStatus() : null);
        bill.setCustomerName(order.getFoodcustomer() != null ? order.getFoodcustomer().getCustomerName() : "Unknown");
        bill.setCustomerPhone(order.getFoodcustomer() != null ? order.getFoodcustomer().getMobile_no() : null);
        List<FoodItemInBillResponse> orderedFoods = order.getOrderItems().stream()
                .map(item -> FoodItemInBillResponse.builder()
                        .FoodName(item.getFood().getFoodName())
                        .FoodPrice(item.getFood().getFoodPrice())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getFood().getFoodPrice() * item.getQuantity())
                        .build())
                .toList();

        bill.setOrderedFoods(orderedFoods);

        System.out.println("Discount: " + discount);
        System.out.println("SubTotal: " + subTotal);
        System.out.println("Coupon: " + coupon);

        order.setBill(bill);

        return billRepository.save(bill);


    }
}
//    @Transactional
//    public Bill generateBillWithDiscount(UUID orderId, UUID discountId, UUID couponId) {
//        OrderFood order = orderFoodRepository.findById(orderId)
//                .orElseThrow(() -> new ConstraintValidationException("Error","Order not found"));
//
//        double subTotal = order.getFoods().stream()
//                .mapToDouble(Food::getFoodPrice)
//                .sum();
//
//        double discountAmount = 0.0;
//        Discount discount = null;
//        Coupon coupon = null;
//
//        // 🔹 Apply Discount (Restaurant level)
//        if (discountId != null) {
//            discount = discountRepository.findById(discountId)
//                    .orElseThrow(() -> new ConstraintValidationException("Error","Invalid discount"));
//            if (discount.isActive() && subTotal >= discount.getMinOrderValue()) {
//                discountAmount = (subTotal * discount.getDiscountPercent()) / 100.0;
//                discountAmount = Math.min(discountAmount, discount.getMaxDiscountAmount());
//            }
//        }
//
//        // 🔹 Apply Coupon (Platform level)
//        if (couponId != null) {
//            coupon = couponRepository.findById(couponId)
//                    .orElseThrow(() -> new ConstraintValidationException("Error","Invalid coupon"));
//            if (coupon.isActive() && subTotal >= coupon.getMinOrderValue()) {
//                double couponDiscount = (subTotal * coupon.getDiscountPercent()) / 100.0;
//                couponDiscount = Math.min(couponDiscount, coupon.getMaxDiscountAmount());
//                discountAmount += couponDiscount;
//            }
//        }
//
//        double finalAmount = subTotal - discountAmount;
//
//        // 🔹 Create Bill
//        Bill bill = Bill.builder()
//                .foodcustomer(order.getFoodcustomer())
//                .hotel(order.getBill().getHotel())
//                .order(order)
//                .subTotal(subTotal)
//                .DiscountAmount(discountAmount)
//                .finalAmount(finalAmount)
//                .paid(false)
//                .discount(discount)
//                .coupon(coupon)
//                .build();
//
//        billRepository.save(bill);
//
//        // 🔹 Update order with final amount
//        order.setTotal_price(subTotal);
//        order.setDiscountAmount(discountAmount);
//        order.setFinalAmount(finalAmount);
//        order.setBill(bill);
//        orderFoodRepository.save(order);
//
//        return bill;
//    }

