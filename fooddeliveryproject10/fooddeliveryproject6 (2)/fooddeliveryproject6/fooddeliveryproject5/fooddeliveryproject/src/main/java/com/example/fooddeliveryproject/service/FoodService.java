package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.*;
import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.ExceptionHandling.CustomerNotFoundException;
import com.example.fooddeliveryproject.RequestBean.FoodRequestBean;
import com.example.RequestBean.PaymentDto;
import com.example.fooddeliveryproject.ResponseBean.FoodResponseBean;
import com.example.fooddeliveryproject.ResponseBean.HotelResponseBean;
import com.example.fooddeliveryproject.repository.*;
import jakarta.persistence.EntityManager;


import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final HotelRepository hotelRepository;
    private final BillRepository billRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<FoodResponseBean> addFoodsInHotel(List<FoodRequestBean> foodDtos) {
        List<Food> foods = foodDtos.stream().map(dto -> {
            Hotel hotel = hotelRepository.findById(dto.getHotelId())
                    .orElseThrow(() -> new ConstraintValidationException("error", "Hotel not found with ID: " + dto.getHotelId()));
            return Food.builder()
                    .foodName(dto.getFoodName())
                    .foodPrice(dto.getFoodPrice())
                    .hotel(hotel)
                    .build();
        }).collect(Collectors.toList());

        List<Food> savedFoods = foodRepository.saveAll(foods);
        return savedFoods.stream()
                .map(f -> FoodResponseBean.builder()
                        .foodId(f.getFood_id())
                        .foodName(f.getFoodName())
                        .foodPrice(f.getFoodPrice())
                        .hotelId(f.getHotel().getId())
                        .hotel_name(f.getHotel().getHotel_name())
                        .hotel_address(f.getHotel().getHotel_address())
                        .build())
                .collect(Collectors.toList());
    }

    public PageImpl<FoodResponseBean> getAllFoods(Pageable pageable) {
        Page<Food> pageResult = foodRepository.findAll(pageable);

        List<FoodResponseBean> foodsList = pageResult.getContent().stream()
                .map(f -> FoodResponseBean.builder()
                        .foodId(f.getFood_id())
                        .foodName(f.getFoodName())
                        .foodPrice(f.getFoodPrice())
                        .hotelId(f.getHotel() != null ? f.getHotel().getId() : null)
                        .hotel_name(f.getHotel() != null ? f.getHotel().getHotel_name() : null)
                        .hotel_address(f.getHotel() != null ? f.getHotel().getHotel_address() : null)
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(foodsList, pageable, pageResult.getTotalElements());
    }

    public FoodResponseBean getFoodById(UUID id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("error", "Food not found with ID: " + id));
        return FoodResponseBean.builder()
                .foodId(food.getFood_id())
                .foodName(food.getFoodName())
                .foodPrice(food.getFoodPrice())
                .hotelId(food.getHotel().getId())
                .hotel_name(food.getHotel().getHotel_name())
                .build();
    }

    public FoodResponseBean updateFood(UUID id, FoodRequestBean dto) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("error", "Food not found with ID: " + id));

        food.setFoodName(dto.getFoodName());
        food.setFoodPrice(dto.getFoodPrice());

        if (dto.getHotelId() != null) {
            Hotel hotel = hotelRepository.findById(dto.getHotelId())
                    .orElseThrow(() -> new ConstraintValidationException("error", "Hotel not found with ID: " + dto.getHotelId()));
            food.setHotel(hotel);
        }

        Food updatedFood = foodRepository.save(food);
        return FoodResponseBean.builder()
                .foodId(updatedFood.getFood_id())
                .foodName(updatedFood.getFoodName())
                .foodPrice(updatedFood.getFoodPrice())
                .hotelId(updatedFood.getHotel().getId())
                .hotel_name(updatedFood.getHotel().getHotel_name() == null ? "" : updatedFood.getHotel().getHotel_name())
                .hotel_address(updatedFood.getHotel().getHotel_address() == null ? null : updatedFood.getHotel().getHotel_address())
                .build();
    }

    public void deleteFood(UUID id) {
        foodRepository.deleteById(id);
    }

    public List<FoodResponseBean> getFoodsByHotel(UUID hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ConstraintValidationException("error", "Hotel not found with ID: " + hotelId));
        return foodRepository.findByHotelId(hotelId).stream()
                .map(f -> FoodResponseBean.builder()
                        .foodId(f.getFood_id())
                        .foodName(f.getFoodName())
                        .foodPrice(f.getFoodPrice())
                        .hotelId(hotel.getId())
                        .hotel_name(hotel.getHotel_name())
                        .build())
                .collect(Collectors.toList());
    }

    public List<FoodResponseBean> searchFoodsInHotel(UUID hotelId, String foodName) {
        return foodRepository.findByHotelIdAndFoodNameContainingIgnoreCase(hotelId, foodName).stream()
                .map(f -> FoodResponseBean.builder()
                        .foodId(f.getFood_id())
                        .foodName(f.getFoodName())
                        .foodPrice(f.getFoodPrice())
                        .hotelId(f.getHotel().getId())
                        .hotel_name(f.getHotel().getHotel_name())
                        .build())
                .collect(Collectors.toList());
    }

    public String deleteFoodById(UUID id) {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
        }
        return "Food Deleted Successfully";
    }

    public String deleteAllFood() {
        foodRepository.deleteAll();
        return "Food Deleted Successfully";
    }

    public PaymentDto makePayment(PaymentDto paymentDto) {
        // 1. Find the bill
        Bill bill = billRepository.findById(paymentDto.getBillId())
                .orElseThrow(() -> new ConstraintValidationException("error", "Bill not found"));

        // Safety: make sure bill has a customer
        FoodCustomer customer = bill.getFoodcustomer();
        if (customer == null) {
            throw new CustomerNotFoundException("error", "Bill has no associated customer");
        }

        // 2. Create Payment entity
        Payment payment = Payment.builder()
                .bill(bill)
                .paymentMode(paymentDto.getPaymentMode())
                .paymentStatus("PAID") // automatically set
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // 3. Link payment to bill
        bill.setPayment(savedPayment);
        billRepository.save(bill);

        // 4. Get order related to this bill
        OrderFood order = orderRepository.findByBill(bill)
                .orElseThrow(() -> new ConstraintValidationException("error", "Order not found for this bill"));

        // Optional: automatically update order status if needed
        if (!order.getOrderStatus().name().equals("CONFIRMED")) {
            order.setOrderStatus(OrderStatus.CONFIRMED); // Assuming you have OrderStatus enum
        }

        // 5. Build PaymentDto response safely
        PaymentDto response = PaymentDto.builder()
                .billId(bill.getBill_id())
                .paymentMode(savedPayment.getPaymentMode())
                .paymentStatus(savedPayment.getPaymentStatus())
                .totalAmount(bill.getTotalAmount())
                .gst(bill.getGst())
                .customerName(customer.getCustomerName())
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus().name())
                .orderTotalPrice(order.getTotal_price())
                .deliveryManId(order.getId())
                .build();
        return response;
    }

//    public List<Food> getAvailableFoods() {
//        return foodRepository.findByAvailableTrue();
//    }
//
//    public List<Food> searchFoodsByName(String keyword) {
//        return foodRepository.findByFoodNameContainingIgnoreCase(keyword);
//    }
//
//    public List<Food> getFoodsByPriceRange(BigDecimal min, BigDecimal max) {
//        return foodRepository.findByFoodPriceBetween(min, max);
//    }
//
//    public List<Food> getFoodsByHotel(String hotelName) {
//        return foodRepository.findByHotel_HotelName(hotelName);
//    }

        public List<Food> getAllFoods() {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Food> cq = cb.createQuery(Food.class);

            Root<Food> food = cq.from(Food.class);
            cq.select(food);

            return entityManager.createQuery(cq).getResultList();
        }
    public List<HotelResponseBean> getHotelsByFoodName(String foodName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hotel> cq = cb.createQuery(Hotel.class);

        Root<Food> food = cq.from(Food.class);

        cq.select(food.get("hotel"))
                .where(cb.equal(cb.lower(food.get("foodName")), foodName.toLowerCase()))
                .distinct(true);

            return entityManager.createQuery(cq).getResultList()
                    .stream()
                    .map(hotel -> new HotelResponseBean(
                            hotel.getId(),
                            hotel.getHotel_name(),
                            hotel.getHotel_phone(),
                            hotel.getHotel_address(),
                            hotel.getFood()
                                    .stream()
                                    .map(f -> new FoodResponseBean(
                                            f.getFood_id(),
                                            f.getFoodName(),
                                            f.getFoodPrice()
                                    ))
                                    .toList()
                    ))
                    .toList();
        }


}

