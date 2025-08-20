package com.example.fooddeliveryproject.controller;

import com.example.fooddeliveryproject.Entity.OrderFood;
import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.CreateOrderByFoodName;
import com.example.fooddeliveryproject.RequestBean.OrderRequestBean;
import com.example.fooddeliveryproject.ResponseBean.OrderResponseBean;
import com.example.fooddeliveryproject.repository.OrderRepository;
import com.example.fooddeliveryproject.service.DeliverymanDetailService;
import com.example.fooddeliveryproject.service.OrderFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderFoodService orderfoodService;
    private final DeliverymanDetailService deliverymanDetailService;
    private final OrderRepository orderRepository;

//    @PostMapping("/create/{customerId}")
//    public ResponseEntity<OrderResponseBean> createOrder(
//            @PathVariable UUID customerId,
//            @RequestBody OrderRequestBean request) {
//
//        // Set customerId from path
//        request.setCustomerId(customerId);
//
//        OrderResponseBean response = orderfoodService.createOrder(request);
//        return ResponseEntity.ok(response);
//    }
//    @PostMapping
//    public ResponseEntity<OrderResponseBean> createOrder(@RequestBody OrderRequestBean request) {
//        OrderResponseBean response = orderfoodService.createOrder(request);
//        return ResponseEntity.ok(response);
//    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseBean> getOrderById(@PathVariable UUID orderId) {
        OrderResponseBean response = orderfoodService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    // Get all orders
    @GetMapping
    public PageImpl<OrderResponseBean> getAllOrders(Pageable pageable) {
        return orderfoodService.getAllOrders(pageable);
    }

    // Update order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseBean> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam OrderStatus newStatus) {
        OrderResponseBean response = orderfoodService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(response);
    }

    // Assign delivery man
//    @PutMapping("/{orderId}/assign-delivery/{deliveryManId}")
//    public ResponseEntity<OrderResponseBean> assignDeliveryMan(
//            @PathVariable UUID orderId,
//            @PathVariable UUID deliveryManId) {
//        OrderResponseBean response = orderfoodService.assignDeliveryMan(orderId, deliveryManId);
//        return ResponseEntity.ok(response);
//    }
    @PatchMapping("/{orderId}/assign-deliveryman/{deliverymanId}")
    public ResponseEntity<OrderFood> assignDeliveryman(
            @PathVariable UUID orderId,
            @PathVariable UUID deliverymanId) {

        OrderFood updatedOrder = orderfoodService.assignDeliverymanToOrder(orderId, deliverymanId);
        return ResponseEntity.ok(updatedOrder);
    }

    // Delete order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID orderId) {
        orderfoodService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully");
    }
//    @PatchMapping("/{orderId}/assign-deliveryman/{id}")
//    public ResponseEntity<OrderFood> assignDeliveryman(
//            @PathVariable UUID orderId,
//            @PathVariable UUID id) {
//
//        OrderFood updatedOrder = orderfoodService.assignDeliverymanToOrder(orderId,id);
//        return ResponseEntity.ok(updatedOrder);
//    }
    @PatchMapping("/{orderId}/assign-deliveryman")
    public ResponseEntity<OrderFood> assignDeliveryman(@PathVariable UUID orderId) {
        OrderFood updatedOrder = orderfoodService.assignNearestDeliveryman(orderId);
        return ResponseEntity.ok(updatedOrder);
    }
    @PostMapping("/assign/{orderId}")
    public ResponseEntity<OrderFood> assignOrder(@PathVariable UUID orderId) {
        OrderFood order = deliverymanDetailService.assignOrderToDeliveryman(
                deliverymanDetailService.orderRepository.findById(orderId)
                        .orElseThrow(() -> new ConstraintValidationException("error","Order not found"))
        );
        return ResponseEntity.ok(order);
    }

    // Mark order as delivered
    @PostMapping("/deliver/{orderId}")
    public ResponseEntity<OrderFood> deliverOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(deliverymanDetailService.markOrderAsDelivered(orderId));
    }
//    @PostMapping
//    public ResponseEntity<OrderFood> placeOrder(@RequestBody OrderFood order) {
//        return ResponseEntity.ok(orderfoodService.placeOrder(order));
//    }
@PostMapping("/create/{customerId}")
public ResponseEntity<OrderResponseBean> createOrder(
        @PathVariable UUID customerId,
        @RequestBody OrderRequestBean request){
    return ResponseEntity.ok(orderfoodService.createOrder(customerId, request));
}

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<OrderResponseBean> cancelOrder(@PathVariable UUID orderId){
        OrderFood cancelled = orderfoodService.cancelOrder(orderId);
        return ResponseEntity.ok(OrderResponseBean.builder()
                .orderId(cancelled.getId())
                .orderStatus(cancelled.getOrderStatus())
                .paid(cancelled.getBill() != null && cancelled.getBill().isPaid())
                .build());
    }

    @PutMapping("/deliver/{orderId}")
    public ResponseEntity<OrderResponseBean> markDelivered(@PathVariable UUID orderId){
        OrderFood delivered = orderfoodService.markDelivered(orderId);
        return ResponseEntity.ok(OrderResponseBean.builder()
                .orderId(delivered.getId())
                .orderStatus(delivered.getOrderStatus())
                .build());
    }
//    @GetMapping("/filter")
//    public List<OrderFood> filterOrders(
//            @RequestParam(required = false) String orderStatus,
//            @RequestParam(required = false) String customerName,
//            @RequestParam(required = false) UUID hotelId,
//            @RequestParam(required = false) String foodName,
//            @RequestParam(required = false) Double minPrice,
//            @RequestParam(required = false) Double maxPrice
//    ) {
//        return filterService.filterOrders(orderStatus, customerName, hotelId, foodName, minPrice, maxPrice);
//    }
}
