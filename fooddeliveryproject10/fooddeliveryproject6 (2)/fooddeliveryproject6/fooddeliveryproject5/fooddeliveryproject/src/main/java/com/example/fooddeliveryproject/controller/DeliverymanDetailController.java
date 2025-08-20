package com.example.fooddeliveryproject.controller;

import com.example.fooddeliveryproject.Entity.DeliverymanDetail;
import com.example.fooddeliveryproject.RequestBean.DeliverymanDetailsRequestBean;
import com.example.fooddeliveryproject.ResponseBean.DeliverymanResponseBean;
import com.example.fooddeliveryproject.service.DeliverymanDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliverymanDetails")
public class DeliverymanDetailController {
    private final DeliverymanDetailService deliverymanService;

    @PostMapping
    public ResponseEntity<DeliverymanResponseBean> createDeliveryman(
            @Valid @RequestBody DeliverymanDetailsRequestBean request) {
        return ResponseEntity.ok(deliverymanService.createDeliveryman(request));
    }

    @GetMapping
    public PageImpl<DeliverymanResponseBean> getAllDeliverymen(Pageable pageable) {
        return deliverymanService.getAllDeliverymen(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliverymanResponseBean> getDeliverymanById(@PathVariable UUID id) {
        return ResponseEntity.ok(deliverymanService.getDeliverymanById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliverymanResponseBean> updateDeliveryman(
            @PathVariable UUID id,
            @Valid @RequestBody DeliverymanDetailsRequestBean request) {
        return ResponseEntity.ok(deliverymanService.updateDeliveryman(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDeliveryman(@PathVariable UUID id) {
        deliverymanService.deleteDeliveryman(id);
        return ResponseEntity.ok("Deliveryman deleted successfully");
    }

}
