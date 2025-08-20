package com.example.fooddeliveryproject.controller;

import com.example.fooddeliveryproject.Entity.Food;
import com.example.fooddeliveryproject.Entity.Hotel;
import com.example.fooddeliveryproject.RequestBean.FoodRequestBean;
import com.example.RequestBean.PaymentDto;
import com.example.fooddeliveryproject.ResponseBean.FoodResponseBean;
import com.example.fooddeliveryproject.ResponseBean.HotelResponseBean;
import com.example.fooddeliveryproject.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {


        private final FoodService foodService;

    @PostMapping("/hotel/add")
    public ResponseEntity<List<FoodResponseBean>> addFoodInHotel(
            @RequestBody List<FoodRequestBean> foodDtos) {
        List<FoodResponseBean> savedFoods = foodService.addFoodsInHotel(foodDtos);
        return ResponseEntity.ok(savedFoods);
    }

    // Add foods without specifying hotel (general)
    @PostMapping("/add")
    public ResponseEntity<List<FoodResponseBean>> addFood(
            @RequestBody List<FoodRequestBean> foodDtos) {
        List<FoodResponseBean> savedFoods = foodService.addFoodsInHotel(foodDtos); // reuse same method
        return ResponseEntity.ok(savedFoods);
    }

    // Get all foods
    @GetMapping("/all")
    public PageImpl<FoodResponseBean> getAllFoods(Pageable pageable) {
        return foodService.getAllFoods(pageable);
    }

    // Get food by ID
    @GetMapping("/{id}")
    public ResponseEntity<FoodResponseBean> getFoodById(@PathVariable UUID id) {
        return ResponseEntity.ok(foodService.getFoodById(id));
    }

    // Get all foods by hotel ID
    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<FoodResponseBean>> getFoodsByHotel(@PathVariable UUID id) {
        return ResponseEntity.ok(foodService.getFoodsByHotel(id));
    }

    // Search foods by name within a hotel
    @GetMapping("/hotel/{id}/search")
    public ResponseEntity<List<FoodResponseBean>> searchFoodsInHotel(
            @PathVariable UUID id,
            @RequestParam String foodName) {
        return ResponseEntity.ok(foodService.searchFoodsInHotel(id, foodName));
    }

    // Update food by ID
    @PutMapping("/{id}")
    public ResponseEntity<FoodResponseBean> updateFood(
            @PathVariable UUID id,
            @RequestBody FoodRequestBean dto) {
        return ResponseEntity.ok(foodService.updateFood(id, dto));
    }

    // Delete food by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable UUID id) {
        return ResponseEntity.ok(foodService.deleteFoodById(id));
    }

    // Delete all foods
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllFoods() {
        return ResponseEntity.ok(foodService.deleteAllFood());
    }
    @PostMapping("/payment")
    public ResponseEntity<PaymentDto> makePayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto response = foodService.makePayment(paymentDto);
        return ResponseEntity.ok(response);
}
//    @GetMapping("/available")
//    public ResponseEntity<List<Food>> getAvailableFoods() {
//        return ResponseEntity.ok(foodService.getAvailableFoods());
//    }
//
//    // Search by name
//    @GetMapping("/search")
//    public ResponseEntity<List<Food>> searchFoods(@RequestParam String keyword) {
//        return ResponseEntity.ok(foodService.searchFoodsByName(keyword));
//    }
//
//    // Filter by price range
//    @GetMapping("/filter/price")
//    public ResponseEntity<List<Food>> filterByPrice(
//            @RequestParam BigDecimal min,
//            @RequestParam BigDecimal max) {
//        return ResponseEntity.ok(foodService.getFoodsByPriceRange(min, max));
//    }
//
//    // Filter by hotel
//    @GetMapping("/filter/hotel")
//    public ResponseEntity<List<Food>> filterByHotel(@RequestParam String hotelName) {
//        return ResponseEntity.ok(foodService.getFoodsByHotel(hotelName));
//    }
//    @GetMapping("/hotel/{hotelName}")
//    public ResponseEntity<List<Food>> getFoodsByHotel(@PathVariable String hotelName) {
//        return ResponseEntity.ok(foodService.getFoodsByHotel(hotelName));
//    }
    @GetMapping("/List")
    public ResponseEntity<List<Food>> getAllFoods() {
        return ResponseEntity.ok(foodService.getAllFoods());
    }
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelResponseBean>> getHotelsByFoodName(@RequestParam String foodName) {
        return ResponseEntity.ok(foodService.getHotelsByFoodName(foodName));
    }
//    @GetMapping("/filter")
//    public ResponseEntity<List<Food>> filterFoods(
//            @RequestParam(required = false) String foodName,
//            @RequestParam(required = false) Double minPrice,
//            @RequestParam(required = false) Double maxPrice,
//            @RequestParam(required = false) String hotelLocation,
//            @RequestParam(required = false) String hotelName
//    ) {
//        return ResponseEntity.ok(
//                foodService.filterFoods(foodName, minPrice, maxPrice, hotelLocation, hotelName)
//        );
//    }
}
