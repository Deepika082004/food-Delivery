package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.DeliverymanDetail;
import com.example.fooddeliveryproject.Entity.DeliverymanRating;
import com.example.fooddeliveryproject.Entity.FoodCustomer;
import com.example.fooddeliveryproject.Entity.OrderFood;
import com.example.fooddeliveryproject.Enum.DeliverymanStatus;
import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.DeliverymanDetailsRequestBean;
import com.example.fooddeliveryproject.RequestBean.DeliverymanFilterRequest;
import com.example.fooddeliveryproject.ResponseBean.DeliverymanResponseBean;
import com.example.fooddeliveryproject.ResponseBean.OrderResponseBean;
import com.example.fooddeliveryproject.repository.DeliverymanDetailsRepository;
import com.example.fooddeliveryproject.repository.DeliverymanRatingRepository;
import com.example.fooddeliveryproject.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliverymanDetailService {
    private final DeliverymanDetailsRepository deliverymanDetailRepository;
    public final OrderRepository orderRepository;
    public final DeliverymanRatingRepository deliverymanRatingRepository;
    public final DeliverymanDetailsRepository deliverymanDetailsRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public DeliverymanResponseBean createDeliveryman(DeliverymanDetailsRequestBean request) {
        DeliverymanDetail entity = DeliverymanDetail.builder()
                .deliveryman_name(request.getDeliveryman_name())
                .deliveryman_phone(request.getDeliveryman_phone())
                .deliveryman_email(request.getDeliveryman_email())
                .deliveryman_address(request.getDeliveryman_address())
                .Licence_no(request.getLicence_no())
                .available(request.getAvailable() != null ? request.getAvailable() : true)
                .status(Boolean.TRUE.equals(request.getAvailable())
                        ? DeliverymanStatus.AVAILABLE
                        : DeliverymanStatus.UNAVAILABLE)
                .locationCal(request.getLocation())
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
        List<DeliverymanRating> ratings = deliverymanRatingRepository.findByDeliverymanDetailId(entity.getId());

        double avgRating;
        if (!ratings.isEmpty()) {
            avgRating = ratings.stream()
                    .mapToDouble(DeliverymanRating::getRating)
                    .average()
                    .orElse(2.0); // fallback
        } else {
            avgRating = 2.0; // ✅ default rating
        }
        return DeliverymanResponseBean.builder()
                .id(entity.getId())
                .deliveryman_name(entity.getDeliveryman_name())
                .deliveryman_phone(entity.getDeliveryman_phone())
                .deliveryman_email(entity.getDeliveryman_email())
                .deliveryman_address(entity.getDeliveryman_address())
                .Licence_no(entity.getLicence_no())
                .available(entity.isAvailable())
                .rating(avgRating)
                .location(entity.getLocationCal())
                .build();
    }
    public OrderFood assignOrderToDeliveryman(OrderFood order) {
        List<DeliverymanDetail> availableDeliverymen = deliverymanDetailRepository.findByAvailableTrue();

        if (availableDeliverymen.isEmpty()) {
            throw new ConstraintValidationException("Error","No deliveryman available at the moment!");
        }

        // For simplicity, we just pick the first available one.
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
    public List<DeliverymanResponseBean> getAvailableHighRatedDeliverymen(
            double minRating, Boolean available, DeliverymanStatus status) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DeliverymanDetail> cq = cb.createQuery(DeliverymanDetail.class);
        Root<DeliverymanDetail> root = cq.from(DeliverymanDetail.class);

        // Join with rating table
        Join<Object, Object> ratingJoin = root.join("deliverymanRating", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        // ✅ Filter by availability if passed
        if (available != null) {
            predicates.add(cb.equal(root.get("available"), available));
        }


        // ✅ Filter by status if passed
        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        // ✅ Filter by rating
        predicates.add(cb.greaterThanOrEqualTo(ratingJoin.get("rating"), minRating));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        List<DeliverymanDetail> results = entityManager.createQuery(cq).getResultList();

        return results.stream()
                .map(this::mapToResponseBean)
                .toList();
    }
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distance in km
    }
    @Transactional
    public void assignNearestDeliveryman(OrderFood order) {
        if(order.getLocationCal() == null) {
            throw new RuntimeException("Customer location not set!");
        }

        List<DeliverymanDetail> availableDeliverymen = deliverymanDetailRepository.findByStatus(DeliverymanStatus.AVAILABLE);

        if (availableDeliverymen.isEmpty()) {
            throw new RuntimeException("No deliveryman available right now!");
        }

        DeliverymanDetail nearest = availableDeliverymen.stream()
                .filter(dm -> dm.getLocationCal() != null)
                .min(Comparator.comparingDouble(dm ->
                        calculateDistance(order.getCustomerLat(), order.getCustomerLng(),
                                dm.getLatitude(), dm.getLongitude())))
                .orElseThrow(() -> new RuntimeException("No deliveryman has location set!"));

        order.setDeliverymanDetail(nearest);
        order.setOrderStatus(OrderStatus.PICKED);
        nearest.setStatus(DeliverymanStatus.BUSY);
        nearest.setAvailable(false);

        orderRepository.save(order);
        deliverymanDetailRepository.save(nearest);
    }

    public OrderFood confirmOrder(UUID orderId) {
        OrderFood order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Order not found"));

        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        assignNearestDeliveryman(order);
        return order;
    }

    public List<DeliverymanDetail> getNearbyDeliverymen(FoodCustomer customer, double maxDistanceKm) {
        List<DeliverymanDetail> allDeliverymen = deliverymanDetailRepository.findAll();
        return allDeliverymen.stream()
                .filter(d -> calculateDistance(customer.getLatitude(), customer.getLongitude(),
                        d.getLatitude(), d.getLongitude()) <= maxDistanceKm)
                .sorted((d1, d2) -> Double.compare(
                        calculateDistance(customer.getLatitude(), customer.getLongitude(),
                                d1.getLatitude(), d1.getLongitude()),
                        calculateDistance(customer.getLatitude(), customer.getLongitude(),
                                d2.getLatitude(), d2.getLongitude())
                ))
                .toList();
    }
    public List<DeliverymanDetail> filterDeliverymen(DeliverymanFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DeliverymanDetail> cq = cb.createQuery(DeliverymanDetail.class);
        Root<DeliverymanDetail> root = cq.from(DeliverymanDetail.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getAvailable() != null) {
            predicates.add(cb.equal(root.get("available"), filter.getAvailable()));
        }
        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }
        if (filter.getName() != null) {
            predicates.add(cb.like(cb.lower(root.get("deliveryman_name")), "%" + filter.getName().toLowerCase() + "%"));
        }
        if (filter.getPhone() != null) {
            predicates.add(cb.like(root.get("deliveryman_phone"), "%" + filter.getPhone() + "%"));
        }
        if (filter.getEmail() != null) {
            predicates.add(cb.like(cb.lower(root.get("deliveryman_email")), "%" + filter.getEmail().toLowerCase() + "%"));
        }
        if (filter.getAddress() != null) {
            predicates.add(cb.like(cb.lower(root.get("deliveryman_address")), "%" + filter.getAddress().toLowerCase() + "%"));
        }
        if (filter.getLicenceNo() != null) {
            predicates.add(cb.like(cb.lower(root.get("Licence_no")), "%" + filter.getLicenceNo().toLowerCase() + "%"));
        }

        // Optional radius filter
        if (filter.getLatitude() != null && filter.getLongitude() != null && filter.getRadiusInKm() != null) {
            Expression<Double> latDiff = cb.diff(root.get("locationCal").get("latitude"), filter.getLatitude());
            Expression<Double> lonDiff = cb.diff(root.get("locationCal").get("longitude"), filter.getLongitude());
            Expression<Double> distanceSquared = cb.sum(cb.prod(latDiff, latDiff), cb.prod(lonDiff, lonDiff));
            predicates.add(cb.le(distanceSquared, Math.pow(filter.getRadiusInKm() / 111.0, 2)));
            // Rough km-to-degree conversion
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }

}


