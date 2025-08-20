package com.example.fooddeliveryproject.controller;

import com.example.fooddeliveryproject.RequestBean.DeliveryRatingRequestBean;
import com.example.fooddeliveryproject.RequestBean.DeliveryRequestBean;
import com.example.fooddeliveryproject.ResponseBean.DeliveryRatingResponseBean;
import com.example.fooddeliveryproject.ResponseBean.DeliveryResponseBean;
import com.example.fooddeliveryproject.service.DeliveryRatingService;
import com.example.fooddeliveryproject.service.DeliveryService;
import com.example.fooddeliveryproject.service.OrderFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class DeliveryController {
    private final OrderFoodService orderFoodService;
    private final DeliveryService deliveryService;
    private final DeliveryRatingService deliveryRatingService;

//    @PatchMapping("/{id}/assignDelivery/{delivery_id}")
//    public ResponseEntity<OrderResponseBean> assignDelivery(
//            @PathVariable UUID id,
//            @PathVariable UUID delivery_id) {
//        OrderResponseBean updatedOrder = orderFoodService.assignDeliveryMan(id, delivery_id);
//        return ResponseEntity.ok(updatedOrder);
//    }
    // ✅ Create delivery using request bean
    @PostMapping
    public ResponseEntity<DeliveryResponseBean> createDelivery(@RequestBody DeliveryRequestBean request) {
        DeliveryResponseBean response = deliveryService.createDelivery(request);
        return ResponseEntity.ok(response);
    }

    // ✅ Get all deliveries
    @GetMapping
    public PageImpl<DeliveryResponseBean> getAllDeliveries(Pageable pageable) {
        return deliveryService.getAllDeliveries(pageable);
    }


    // ✅ Get delivery by ID
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseBean> getDeliveryById(@PathVariable UUID id) {
        return deliveryService.getDeliveryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Update delivery
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponseBean> updateDelivery(
            @PathVariable UUID id,
            @RequestBody DeliveryRequestBean request) {
        DeliveryResponseBean response = deliveryService.updateDelivery(id, request);
        return ResponseEntity.ok(response);
    }

    // ✅ Delete delivery
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDelivery(@PathVariable UUID id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.ok("Delivery deleted successfully");
    }

    // ✅ Rate Deliveryman
    @PostMapping("/rate/{customerId}")
    public ResponseEntity<DeliveryRatingResponseBean> rateDeliveryMan(
            @PathVariable UUID id,
            @RequestBody DeliveryRatingRequestBean request) {

        DeliveryRatingResponseBean response = deliveryService.rateDeliveryMan(id, request);
        return ResponseEntity.ok(response);
    }

    // ✅ Get top-rated deliverymen
    @GetMapping("/top-rated")
    public ResponseEntity<List<DeliveryResponseBean>> getTopRatedDeliverymen(@RequestParam int rating) {
        return ResponseEntity.ok(deliveryService.getDeliverymenByRating(rating));
    }
    @GetMapping("/rating")
    public ResponseEntity<List<DeliveryResponseBean>> getDeliverymenByRating(
            @RequestParam int rating) {
        List<DeliveryResponseBean> deliverymen = deliveryService.getDeliverymenByRating(rating);
        return ResponseEntity.ok(deliverymen);
    }

//    @PostMapping("/nearby")
//    public List<DeliverymanDistanceDto> getNearbyDeliveryMen(@RequestBody Location pickupLocation) {
//        return deliveryService.getNearbyDeliveryMen(pickupLocation);
//    }
}
