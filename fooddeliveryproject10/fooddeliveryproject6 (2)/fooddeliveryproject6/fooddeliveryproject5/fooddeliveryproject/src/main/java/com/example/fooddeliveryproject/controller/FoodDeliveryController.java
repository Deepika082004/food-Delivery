package com.example.fooddeliveryproject.controller;

import com.example.ConstantMesage.ErrorResponse;
import com.example.RequestBean.OrderDto;
import com.example.fooddeliveryproject.Entity.*;
import com.example.fooddeliveryproject.Enum.PaymentMode;
import com.example.fooddeliveryproject.RequestBean.*;
import com.example.fooddeliveryproject.ResponseBean.CustomerResponseBean;
import com.example.fooddeliveryproject.service.FoodDeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class FoodDeliveryController {
    @Autowired
    private FoodDeliveryService foodDeliveryService;

    @GetMapping()
    public PageImpl<CustomerResponseBean> getAllHostels(Pageable pageable) {
        return foodDeliveryService.getAllHotels(pageable);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseBean> getHotelById(@PathVariable UUID id) {
        return ResponseEntity.ok(foodDeliveryService.getCustomerById(id));
    }


//    @GetMapping("/Deliveryrating/{id}")
//    public ResponseEntity<DeliverymanRatingDto> getDeliveymanRating(@PathVariable UUID id) {
//        DeliverymanRatingDto ratingDto = foodDeliveryService.getDeliverymanRating(id);
//        return ResponseEntity.ok(ratingDto);
//    }
//    @GetMapping("/Deliveryrating")
//    public ResponseEntity<List<DeliverymanRatingDto>> getAllDeliverymanRatings() {
//        List<DeliverymanRatingDto> ratings = foodDeliveryService.getAllDeliverymanRatings();
//        return ResponseEntity.ok(ratings);
//    }
//
//    @PostMapping("/add-deliveryrating")
//    public ResponseEntity<DeliverymanRatingDto> addRating(@Valid @RequestBody DeliverymanRatingDto dto) {
//        return ResponseEntity.ok(foodDeliveryService.addDeliveryRating(dto));
//    }
//
//
//    @GetMapping("/Payment/{Bill_id}")
//    public ResponseEntity<PaymentDto> getPayment(@PathVariable UUID Bill_id) {
//        PaymentDto paymentDto = foodDeliveryService.getPayment(Bill_id);
//        return ResponseEntity.ok(paymentDto);
//    }
//
//
//    @PostMapping("/payment")
//    public ResponseEntity<PaymentDto> addRating(@Valid @RequestBody PaymentDto dto) {
//        return ResponseEntity.ok(foodDeliveryService.addPayment(dto));
//    }


//    @GetMapping("/Bill/{Order_id}")
//    public ResponseEntity<BillDto> getBill(@PathVariable UUID id) {
//        BillDto billDto = foodDeliveryService.getBill(id);
//        return ResponseEntity.ok(billDto);
//    }
//

    @PostMapping
    public ErrorResponse addData(@Valid @RequestBody CustomerRequestBean requestDto) {
        return foodDeliveryService.addData(requestDto);
    }

    @GetMapping("/filterByCustomer")
    public List<OrderFood> getOrdersByCustomer(@RequestParam String customerName) {
        return foodDeliveryService.getOrdersByCustomerName(customerName);
    }

    @PostMapping("/bulk")
    public ErrorResponse addMoreData(@Valid @RequestBody List<CustomerRequestBean> requestDtoList) {
        return foodDeliveryService.saveAllData(requestDtoList);
    }
    @GetMapping("/hotels/search/filter")
    public List<Hotel> filterHotels(
            @RequestParam(required = false) Integer minOrders,
            @RequestParam(required = false) Integer minFoods,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer minDeliveries
    ) {
        return foodDeliveryService.filterHotels(minOrders, minFoods, minRating, minDeliveries);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateHostel(@PathVariable UUID id,
                                               @RequestBody CustomerRequestBean requestDto) {
        return ResponseEntity.ok(foodDeliveryService.updateFood(id, requestDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable UUID id) {
        return ResponseEntity.ok(foodDeliveryService.deleteFood(id));
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllFood() {

        return ResponseEntity.ok(foodDeliveryService.deleteAllFood());
    }
    @GetMapping("/orders/filter")
    public ResponseEntity<List<OrderDto>> filterOrders(
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String foodName
    ) {
        List<OrderDto> filteredOrders = foodDeliveryService.filterOrders(orderStatus, customerName, minPrice, maxPrice, foodName);
        return ResponseEntity.ok(filteredOrders);
    }
    @GetMapping("/filters")
    public ResponseEntity<List<Food>> filterFood(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false, name = "hotel_name") String hotel_name,
            @RequestParam(required = false, name = "food_name") String foodName) {

        List<Food> foods = foodDeliveryService.filterFood(id, hotel_name, foodName);
        return ResponseEntity.ok(foods);
    }

    @GetMapping("/bill/filterByPayment")
    public List<Bill> filterBills(@RequestParam PaymentMode paymentMode) {
        return foodDeliveryService.filterBillsByPaymentMode(paymentMode);
    }
    @GetMapping("/filter")
    public List<FoodCustomer> filterCustomers(
            @RequestParam(required = true) String customerName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false) String foodName,
            @RequestParam(required = false) String locationName
    ) {
        return foodDeliveryService.filterCustomers(customerName, email, hotelName, foodName, locationName);
    }
}

