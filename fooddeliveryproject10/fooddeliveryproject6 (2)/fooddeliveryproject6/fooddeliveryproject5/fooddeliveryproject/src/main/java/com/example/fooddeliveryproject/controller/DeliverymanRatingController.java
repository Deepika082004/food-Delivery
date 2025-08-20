package com.example.fooddeliveryproject.controller;

import com.example.fooddeliveryproject.RequestBean.DeliveryRatingRequestBean;
import com.example.fooddeliveryproject.ResponseBean.DeliveryRatingResponseBean;
import com.example.fooddeliveryproject.service.DeliveryRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveryRating")
public class DeliverymanRatingController {
    private final DeliveryRatingService ratingService;

    // Create Rating
    @PostMapping
    public ResponseEntity<DeliveryRatingResponseBean> createRating(
            @Valid @RequestBody DeliveryRatingRequestBean request) {
        return ResponseEntity.ok(ratingService.createRating(request));
    }

    @GetMapping
    public PageImpl<DeliveryRatingResponseBean> getAllRatings(Pageable pageable) {
        return ratingService.getAllRatings(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryRatingResponseBean> getRatingById(@PathVariable UUID id) {
        return ResponseEntity.ok(ratingService.getRatingById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryRatingResponseBean> updateRating(
            @PathVariable UUID id,
            @Valid @RequestBody DeliveryRatingRequestBean request) {
        return ResponseEntity.ok(ratingService.updateRating(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable UUID id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok("Rating deleted successfully");
    }

}
