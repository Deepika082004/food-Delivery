package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.DeliverymanDetail;
import com.example.fooddeliveryproject.Entity.DeliverymanRating;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.DeliveryRatingRequestBean;
import com.example.fooddeliveryproject.ResponseBean.DeliveryRatingResponseBean;
import com.example.fooddeliveryproject.repository.DeliverymanDetailsRepository;
import com.example.fooddeliveryproject.repository.DeliverymanRatingRepository;
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
public class DeliveryRatingService {
    private final DeliverymanRatingRepository ratingRepository;
    private final DeliverymanDetailsRepository detailRepository;

    // Convert Entity → DTO
    public DeliveryRatingResponseBean createRating(DeliveryRatingRequestBean request) {
        DeliverymanDetail deliveryman = detailRepository.findById(request.getDeliverymanDetailId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Deliveryman not found"));

        DeliverymanRating rating = DeliverymanRating.builder()
                .rating(Integer.parseInt(String.valueOf(request.getRating())))
                .feedback(request.getFeedback())
                .deliverymanDetail(deliveryman)
                .build();

        DeliverymanRating saved = ratingRepository.save(rating);
        return mapToResponseBean(saved);
    }

    // Get all ratings
    public PageImpl<DeliveryRatingResponseBean> getAllRatings(Pageable pageable) {
        Page<DeliverymanRating> pageResult = ratingRepository.findAll(pageable);

        List<DeliveryRatingResponseBean> ratingsList = pageResult.getContent().stream()
                .map(this::mapToResponseBean)
                .collect(Collectors.toList());

        return new PageImpl<>(ratingsList, pageable, pageResult.getTotalElements());
    }

    // Get rating by ID
    public DeliveryRatingResponseBean getRatingById(UUID id) {
        DeliverymanRating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Rating not found"));
        return mapToResponseBean(rating);
    }

    // Update rating
    public DeliveryRatingResponseBean updateRating(UUID id, DeliveryRatingRequestBean request) {
        DeliverymanRating existing = ratingRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("error","Rating not found"));

        DeliverymanDetail deliveryman = detailRepository.findById(request.getDeliverymanDetailId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Deliveryman not found"));

        existing.setRating(Integer.parseInt(String.valueOf(request.getRating())));
        existing.setFeedback(request.getFeedback());
        existing.setDeliverymanDetail(deliveryman);

        DeliverymanRating updated = ratingRepository.save(existing);
        return mapToResponseBean(updated);
    }

    // Delete rating
    public void deleteRating(UUID id) {
        if (!ratingRepository.existsById(id)) {
            throw new ConstraintValidationException("Error","Rating not found");
        }
        ratingRepository.deleteById(id);
    }

    // Mapper
    private DeliveryRatingResponseBean mapToResponseBean(DeliverymanRating rating) {
        return DeliveryRatingResponseBean.builder()
                .ratingId(rating.getId())
                .rating(String.valueOf(Integer.valueOf(rating.getRating())))
                .feedback(rating.getFeedback())
                .deliverymanId(rating.getDeliverymanDetail().getId())
                .deliverymanName(rating.getDeliverymanDetail().getDeliveryman_name())
                .build();
    }
}
