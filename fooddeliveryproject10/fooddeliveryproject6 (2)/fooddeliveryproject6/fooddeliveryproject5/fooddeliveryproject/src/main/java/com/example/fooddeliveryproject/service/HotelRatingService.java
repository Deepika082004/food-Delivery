package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.FoodCustomer;
import com.example.fooddeliveryproject.Entity.Hotel;
import com.example.fooddeliveryproject.Entity.Hotel_Rating;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.HotelRatingRequestBean;
import com.example.fooddeliveryproject.ResponseBean.HotelRatingResponseBean;
import com.example.fooddeliveryproject.repository.CustomerRepository;
import com.example.fooddeliveryproject.repository.HotelRatingRepository;
import com.example.fooddeliveryproject.repository.HotelRepository;
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
public class HotelRatingService {
    private final HotelRatingRepository hotelRatingRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository foodCustomerRepository;

    // Convert Entity → DTO
    private HotelRatingResponseBean toResponse(Hotel_Rating rating) {
        return HotelRatingResponseBean.builder()
                .id(rating.getId())
                .rating(rating.getRating())
                .comment(rating.getComment())
                .hotelId(rating.getHotel() != null ? rating.getHotel().getId() : null)
                .hotelName(rating.getHotel() != null ? rating.getHotel().getHotel_name() : null)
                .customerId(rating.getCustomer() != null ? rating.getCustomer().getId() : null)
                .customerName(rating.getCustomer() != null ? rating.getCustomer().getCustomerName() : null)
                .build();
    }

    // Convert Request Bean → Entity
    private Hotel_Rating toEntity(HotelRatingRequestBean request) {
        Hotel_Rating rating = Hotel_Rating.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        // Set Hotel
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel not found"));
        rating.setHotel(hotel);

        // Set Customer
        FoodCustomer customer = foodCustomerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Customer not found"));
        rating.setCustomer(customer);

        return rating;
    }

    // Create Rating
    public HotelRatingResponseBean createRating(HotelRatingRequestBean request) {
        Hotel_Rating rating = toEntity(request);
        return toResponse(hotelRatingRepository.save(rating));
    }

    // Get All Ratings
    public PageImpl<HotelRatingResponseBean> getAllRatings(Pageable pageable) {
        Page<Hotel_Rating> pageResult = hotelRatingRepository.findAll(pageable);

        List<HotelRatingResponseBean> ratingsList = pageResult.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(ratingsList, pageable, pageResult.getTotalElements());
    }

    // Get Rating by ID
    public HotelRatingResponseBean getRatingById(UUID id) {
        Hotel_Rating rating = hotelRatingRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel rating not found"));
        return toResponse(rating);
    }

    // Update Rating
    public HotelRatingResponseBean updateRating(UUID id, HotelRatingRequestBean request) {
        Hotel_Rating existing = hotelRatingRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel rating not found"));

        existing.setRating(request.getRating());
        existing.setComment(request.getComment());

        // Update hotel
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel not found"));
        existing.setHotel(hotel);

        // Update customer
        FoodCustomer customer = foodCustomerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Customer not found"));
        existing.setCustomer(customer);

        return toResponse(hotelRatingRepository.save(existing));
    }

    // Delete Rating
    public void deleteRating(UUID id) {
        Hotel_Rating rating = hotelRatingRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel rating not found"));
        hotelRatingRepository.delete(rating);
    }

}
