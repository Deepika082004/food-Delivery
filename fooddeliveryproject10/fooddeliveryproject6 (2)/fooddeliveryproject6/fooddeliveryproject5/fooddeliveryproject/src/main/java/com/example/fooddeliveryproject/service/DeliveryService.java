package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.*;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.DeliveryRatingRequestBean;
import com.example.fooddeliveryproject.RequestBean.DeliveryRequestBean;
import com.example.RequestBean.DeliverymanRatingDto;
//import com.example.fooddeliveryproject.RequestBean.DistanceUtils;
import com.example.fooddeliveryproject.ResponseBean.DeliveryRatingResponseBean;
import com.example.fooddeliveryproject.ResponseBean.DeliveryResponseBean;
import com.example.fooddeliveryproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliverymanRatingRepository deliverymanRatingRepository;
    private final DeliverymanDetailsRepository deliverymanDetailsRepository;
    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final CustomerRepository customerRepository;
    public DeliveryRatingResponseBean rateDeliveryMan(UUID customerId, DeliveryRatingRequestBean request) {
        // Check if deliveryman exists
        DeliverymanDetail deliveryman = deliverymanDetailsRepository.findById(request.getDeliverymanDetailId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Deliveryman not found"));

        // Check if customer exists
        FoodCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Customer not found"));

        // Save rating
        DeliverymanRating rating = DeliverymanRating.builder()
                .deliverymanDetail(deliveryman)
                .rating(request.getRating())
                .feedback(request.getFeedback())
                .build();

        deliverymanRatingRepository.save(rating);

        // Build response
        return DeliveryRatingResponseBean.builder()
                //.message("Rating submitted successfully")
                .deliverymanName(deliveryman.getDeliveryman_name())   // assuming you have `name` in DeliverymanDetail
                .rating(String.valueOf(rating.getRating()))
                .feedback(rating.getFeedback())
                .build();
    }
        // Fetch ratings matching the given rating
        public DeliveryResponseBean createDelivery(DeliveryRequestBean request) {
            FoodCustomer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ConstraintValidationException("Error","Customer not found"));

            Food food = foodRepository.findById(request.getFoodId())
                    .orElseThrow(() -> new ConstraintValidationException("Error","Food not found"));

            DeliverymanDetail deliverymanDetail = null;
            if (request.getDeliverymanDetailId() != null) {
                deliverymanDetail = deliverymanDetailsRepository.findById(request.getDeliverymanDetailId())
                        .orElseThrow(() -> new ConstraintValidationException("Error","Deliveryman not found"));
            }

            Delivery delivery = Delivery.builder()
                    .foodcustomer(customer)
                    .food_name(food.getFoodName())
                    .customer_name(customer.getCustomerName())
                    .email(customer.getEmail())
                    .mobile_phone(customer.getMobile_no())
                    .deliverymanDetail(deliverymanDetail)
                    .build();

            Delivery savedDelivery = deliveryRepository.save(delivery);

            return mapToResponse(savedDelivery, customer, food, deliverymanDetail);
        }

    // ✅ Get all deliveries
    public PageImpl<DeliveryResponseBean> getAllDeliveries(Pageable pageable) {
        Page<Delivery> pageResult = deliveryRepository.findAll(pageable); // fetch paginated data

        List<DeliveryResponseBean> deliveryList = pageResult.getContent().stream()
                .map(d -> mapToResponse(d, d.getFoodcustomer(), d.getFood(), d.getDeliverymanDetail()))
                .collect(Collectors.toList());

        return new PageImpl<>(deliveryList, pageable, pageResult.getTotalElements());
    }

    // ✅ Get delivery by ID
    public Optional<DeliveryResponseBean> getDeliveryById(UUID id) {
        return deliveryRepository.findById(id)
                .map(d -> mapToResponse(d, d.getFoodcustomer(), d.getFood(), d.getDeliverymanDetail()));
    }

    // ✅ Update delivery
    public DeliveryResponseBean updateDelivery(UUID id, DeliveryRequestBean request) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Delivery not found with id " + id));

        FoodCustomer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Customer not found"));

        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Food not found"));

        DeliverymanDetail deliverymanDetail = null;
        if (request.getDeliverymanDetailId() != null) {
            deliverymanDetail = deliverymanDetailsRepository.findById(request.getDeliverymanDetailId())
                    .orElseThrow(() -> new ConstraintValidationException("error","Deliveryman not found"));
        }

        delivery.setFoodcustomer(customer);
        delivery.setFood_name(food.getFoodName());
        delivery.setCustomer_name(customer.getCustomerName());
        delivery.setEmail(customer.getEmail());
        delivery.setMobile_phone(customer.getMobile_no());
        delivery.setDeliverymanDetail(deliverymanDetail);

        Delivery updatedDelivery = deliveryRepository.save(delivery);

        return mapToResponse(updatedDelivery, customer, food, deliverymanDetail);
    }

    // ✅ Delete delivery
    public void deleteDelivery(UUID id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Delivery not found with id " + id));
        deliveryRepository.delete(delivery);
    }

    // ✅ Rate Deliveryman
    public DeliveryRatingResponseBean rateDeliveryMan(UUID customerId, DeliverymanRatingDto dto) {
        FoodCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Customer not found"));

        DeliverymanDetail deliverymanDetail = deliverymanDetailsRepository.findById(dto.getId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Deliveryman not found"));

        DeliverymanRating rating = DeliverymanRating.builder()
                .deliverymanDetail(deliverymanDetail)
                .rating(Integer.parseInt(dto.getRating()))
                .feedback(dto.getFeedback())
                .build();

        DeliverymanRating savedRating = deliverymanRatingRepository.save(rating);

        return DeliveryRatingResponseBean.builder()
                .ratingId(savedRating.getId())
                .deliverymanId(deliverymanDetail.getId())
                .deliverymanName(deliverymanDetail.getDeliveryman_name())
                .rating(String.valueOf(savedRating.getRating()))
                .feedback(savedRating.getFeedback())
                .build();
    }

    // ✅ Get deliverymen by rating
    public List<DeliveryResponseBean> getDeliverymenByRating(int ratingValue) {
        return deliverymanRatingRepository.findByRating(ratingValue)
                .stream()
                .map(r -> mapToResponse(r.getDeliverymanDetail().getDelivery(),
                        r.getDeliverymanDetail().getDelivery().getFoodcustomer(),
                        r.getDeliverymanDetail().getDelivery().getFood(),
                        r.getDeliverymanDetail()))
                .toList();
    }

    // ✅ Mapper (Entity → ResponseBean)
    private DeliveryResponseBean mapToResponse(Delivery delivery, FoodCustomer customer, Food food, DeliverymanDetail deliverymanDetail) {
        return DeliveryResponseBean.builder()
                .deliveryId(delivery.getDelivery_id())
                .customerId(customer.getId())
                .customerName(customer.getCustomerName())
                .customerEmail(customer.getEmail())
                .customerPhone(customer.getMobile_no())
                .foodId(food.getFood_id())
                .foodName(food.getFoodName())
                .foodPrice(food.getFoodPrice())
                .deliverymanDetailId(deliverymanDetail != null ? deliverymanDetail.getId() : null)
                .deliverymanName(deliverymanDetail != null ? deliverymanDetail.getDeliveryman_name() : null)
                .deliverymanPhone(deliverymanDetail != null ? deliverymanDetail.getDelivery().getMobile_phone() : null)
                .build();
    }
  //  private static final double MAX_DISTANCE_KM = 5.0; // Nearby threshold

//    public List<DeliverymanDistanceDto> getNearbyDeliveryMen(Location pickupLocation) {
//        List<DeliverymanDetail> availableMen = deliverymanDetailsRepository.findByAvailableTrue();
//
//        return availableMen.stream()
//                .map(dm -> {
////                    double distance = DistanceUtils.calculateDistance(dm.getCurrentLocation(), pickupLocation);
////                    return new DeliverymanDistanceDto(dm.getId(), dm.getDeliveryman_name(), dm.getCurrentLocation(), distance);
//                })
//                .filter(dto -> dto.getDistanceKm() <= MAX_DISTANCE_KM) // Only nearby
//                .sorted(Comparator.comparingDouble(DeliverymanDistanceDto::getDistanceKm)) // Closest first
//                .toList();
//    }

}
