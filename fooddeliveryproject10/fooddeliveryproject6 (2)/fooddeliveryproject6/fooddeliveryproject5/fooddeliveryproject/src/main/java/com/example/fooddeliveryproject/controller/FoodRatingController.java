package com.example.fooddeliveryproject.controller;

import com.example.fooddeliveryproject.RequestBean.FoodRatingRequestBean;
import com.example.fooddeliveryproject.ResponseBean.FoodRatingResponseBean;
import com.example.fooddeliveryproject.service.FoodRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/foodrating")
public class FoodRatingController {
    private final FoodRatingService foodRatingService;

    // CREATE
    @PostMapping
    public ResponseEntity<FoodRatingResponseBean> createRating(@Valid @RequestBody FoodRatingRequestBean dto) {
        return ResponseEntity.ok(foodRatingService.createRating(dto));
    }

    // READ - by ID
    @GetMapping("/{id}")
    public ResponseEntity<FoodRatingResponseBean> getRatingById(@PathVariable UUID id) {
        return ResponseEntity.ok(foodRatingService.getRatingById(id));
    }

    // READ - all
    @GetMapping
    public ResponseEntity<List<FoodRatingResponseBean>> getAllRatings() {
        return ResponseEntity.ok(foodRatingService.getAllRatings());
    }

    // READ - by Food
//    @GetMapping("/food/{food_id}")
//    public ResponseEntity<List<FoodRatingResponseBean>> getRatingsByFood(@PathVariable UUID foodId) {
//        return ResponseEntity.ok(foodRatingService.getRatingsByFood(foodId));
//    }
//
//    // READ - by Customer
//    @GetMapping("/customer/{id}")
//    public ResponseEntity<List<FoodRatingResponseBean>> getRatingsByCustomer(@PathVariable UUID customerId) {
//        return ResponseEntity.ok(foodRatingService.getRatingsByCustomer(customerId));
//    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<FoodRatingResponseBean> updateRating(
            @PathVariable UUID id,
            @RequestBody FoodRatingRequestBean dto
    ) {
        return ResponseEntity.ok(foodRatingService.updateRating(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable UUID id) {
        foodRatingService.deleteRating(id);
        return ResponseEntity.ok("Rating deleted successfully");
    }
    @PutMapping("{customerId}/food/{foodId}")
    public ResponseEntity<FoodRatingResponseBean> updateRatingByCustomer(
            @PathVariable UUID customerId,
            @PathVariable UUID foodId,
            @RequestBody FoodRatingRequestBean dto
    ) {
        dto.setCustomerId(customerId);
        dto.setFoodId(foodId);
        return ResponseEntity.ok(foodRatingService.updateRatingByCustomer(dto));
    }

}
