package com.example.fooddeliveryproject.controller;

import com.example.fooddeliveryproject.RequestBean.HotelRatingRequestBean;
import com.example.fooddeliveryproject.ResponseBean.HotelRatingResponseBean;
import com.example.fooddeliveryproject.service.HotelRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotelRating")
public class HotelRatingController {
    private final HotelRatingService hotelRatingService;

    @PostMapping
    public ResponseEntity<HotelRatingResponseBean> createRating(@Valid @RequestBody HotelRatingRequestBean request) {
        HotelRatingResponseBean response = hotelRatingService.createRating(request);
        return ResponseEntity.ok(response);
    }

    // Get All Ratings
    @GetMapping
    public PageImpl<HotelRatingResponseBean> getAllRatings(Pageable pageable) {
        return hotelRatingService.getAllRatings(pageable);
    }


    // Get Rating by ID
    @GetMapping("/{id}")
    public ResponseEntity<HotelRatingResponseBean> getRatingById(@PathVariable UUID id) {
        HotelRatingResponseBean rating = hotelRatingService.getRatingById(id);
        return ResponseEntity.ok(rating);
    }

    // Update Rating
    @PutMapping("/{id}")
    public ResponseEntity<HotelRatingResponseBean> updateRating(
            @PathVariable UUID id,
            @RequestBody HotelRatingRequestBean request) {
        HotelRatingResponseBean updated = hotelRatingService.updateRating(id, request);
        return ResponseEntity.ok(updated);
    }

    // Delete Rating
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable UUID id) {
        hotelRatingService.deleteRating(id);
        return ResponseEntity.ok("Hotel rating deleted successfully");
    }
}
