package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.DeliverymanDetail;
import com.example.fooddeliveryproject.Entity.OrderFood;
import com.example.fooddeliveryproject.Enum.DeliverymanStatus;
import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.DeliverymanDetailsRequestBean;
import com.example.fooddeliveryproject.ResponseBean.DeliverymanResponseBean;
import com.example.fooddeliveryproject.repository.DeliverymanDetailsRepository;
import com.example.fooddeliveryproject.repository.OrderRepository;
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
public class DeliverymanDetailService {
    private final DeliverymanDetailsRepository deliverymanDetailRepository;
    public final OrderRepository orderRepository;

    public DeliverymanResponseBean createDeliveryman(DeliverymanDetailsRequestBean request) {
        DeliverymanDetail entity = DeliverymanDetail.builder()
                .deliveryman_name(request.getDeliveryman_name())
                .deliveryman_phone(request.getDeliveryman_phone())
                .deliveryman_email(request.getDeliveryman_email())
                .deliveryman_address(request.getDeliveryman_address())
                .Licence_no(request.getLicence_no())
                .locationCal(request.getLocation())
                .available(request.getAvailable())
                .build();

        DeliverymanDetail saved = deliverymanDetailRepository.save(entity);
        return mapToResponseBean(saved);
    }

    // Get all deliverymen
    public PageImpl<DeliverymanResponseBean> getAllDeliverymen(Pageable pageable) {
        Page<DeliverymanDetail> pageResult = deliverymanDetailRepository.findAll(pageable);

        List<DeliverymanResponseBean> deliverymenList = pageResult.getContent().stream()
                .map(this::mapToResponseBean)
                .collect(Collectors.toList());

        return new PageImpl<>(deliverymenList, pageable, pageResult.getTotalElements());
    }

    // Get deliveryman by ID
    public DeliverymanResponseBean getDeliverymanById(UUID id) {
        DeliverymanDetail deliveryman = deliverymanDetailRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("error","Deliveryman not found"));
        return mapToResponseBean(deliveryman);
    }

    // Update deliveryman
    public DeliverymanResponseBean updateDeliveryman(UUID id, DeliverymanDetailsRequestBean request) {
        DeliverymanDetail existing = deliverymanDetailRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Deliveryman not found"));

        existing.setDeliveryman_name(request.getDeliveryman_name());
        existing.setDeliveryman_phone(request.getDeliveryman_phone());
        existing.setDeliveryman_email(request.getDeliveryman_email());
        existing.setDeliveryman_address(request.getDeliveryman_address());
        existing.setLicence_no(request.getLicence_no());
        existing.setLocationCal(request.getLocation());

        DeliverymanDetail updated = deliverymanDetailRepository.save(existing);
        return mapToResponseBean(updated);
    }

    // Delete deliveryman
    public void deleteDeliveryman(UUID id) {
        if (!deliverymanDetailRepository.existsById(id)) {
            throw new ConstraintValidationException("Error","Deliveryman not found");
        }
        deliverymanDetailRepository.deleteById(id);
    }

    // Mapper method
    private DeliverymanResponseBean mapToResponseBean(DeliverymanDetail entity) {
        return DeliverymanResponseBean.builder()
                .id(entity.getId())
                .deliveryman_name(entity.getDeliveryman_name())
                .deliveryman_phone(entity.getDeliveryman_phone())
                .deliveryman_email(entity.getDeliveryman_email())
                .deliveryman_address(entity.getDeliveryman_address())
                .Licence_no(entity.getLicence_no())
                .available(entity.isAvailable())
                .build();
    }
    public OrderFood assignOrderToDeliveryman(OrderFood order) {
        List<DeliverymanDetail> availableDeliverymen = deliverymanDetailRepository.findByAvailableTrue();

        if (availableDeliverymen.isEmpty()) {
            throw new ConstraintValidationException("Error","No deliveryman available at the moment!");
        }

        // For simplicity, we just pick the first available one.
        // You can implement distance calculation using locationCal if you want nearest.
        DeliverymanDetail deliveryman = availableDeliverymen.get(0);

        // Assign order to deliveryman
        deliveryman.setOrderFood(order);
        deliveryman.setAvailable(false);
        deliveryman.setStatus(DeliverymanStatus.ON_DELIVERY);

        order.setDeliverymanDetail(deliveryman);
        order.setOrderStatus(OrderStatus.CONFIRMED); // Order confirmed when assigned

        deliverymanDetailRepository.save(deliveryman);
        return orderRepository.save(order);
    }

    // Mark order as delivered
    public OrderFood markOrderAsDelivered(UUID orderId) {
        Optional<OrderFood> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new ConstraintValidationException("Error","Order not found!");
        }

        OrderFood order = optionalOrder.get();
        order.setOrderStatus(OrderStatus.DELIVERED);

        // Update deliveryman availability
        DeliverymanDetail deliveryman = order.getDeliverymanDetail();
        if (deliveryman != null) {
            deliveryman.setAvailable(true);
            deliveryman.setStatus(DeliverymanStatus.AVAILABLE);
            deliveryman.setOrderFood(null); // clear the assigned order
            deliverymanDetailRepository.save(deliveryman);
        }

        return orderRepository.save(order);
    }
}
