package com.example.fooddeliveryproject.service;


import com.example.fooddeliveryproject.Entity.*;
import com.example.fooddeliveryproject.Enum.DeliverymanStatus;
import com.example.fooddeliveryproject.Enum.OrderStatus;
//import com.example.fooddeliveryproject.RequestBean.DistanceUtils;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.RequestBean.OrderRequestBean;
import com.example.fooddeliveryproject.ResponseBean.OrderResponseBean;
import com.example.fooddeliveryproject.repository.*;
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
public class OrderFoodService {
    private final CustomerRepository foodCustomerRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderFoodRepository;
    private final BillRepository billRepository;
    private final DeliveryRepository deliveryRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final DeliverymanDetailsRepository deliverymanDetailsRepository;

    @PersistenceContext
    private EntityManager entityManager;
    private OrderResponseBean toResponse(OrderFood order) {
        List<String> foodNames = Optional.ofNullable(order.getFoods())
                .orElse(List.of())
                .stream()
                .map(Food::getFoodName)
                .toList();

        DeliverymanDetail deliveryman = order.getDeliverymanDetail();
        double totalPrice = order.getFoods().stream()
                .mapToDouble(Food::getFoodPrice)
                .sum();

        double gst = totalPrice * 0.05; // 5% GST
        double totalWithGst = totalPrice + gst;

        return OrderResponseBean.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .customerId(order.getFoodcustomer() != null ? order.getFoodcustomer().getId() : null)
                .customerName(order.getFoodcustomer() != null ? order.getFoodcustomer().getCustomerName() : null)
                .customerPhone(order.getFoodcustomer() != null ? order.getFoodcustomer().getMobile_no() : null)
                .hotelId(order.getHotel() != null ? order.getHotel().getId() : null)
                .hotelName(order.getHotel() != null ? order.getHotel().getHotel_name() : null)
                .hotelAddress(order.getHotel() != null ? order.getHotel().getHotel_address() : null)
                .foodNames(foodNames)
                .totalPrice(order.getTotal_price())
                .gst(gst)
                .totalAmountWithGST(totalWithGst)
                .billId(order.getBill() != null ? order.getBill().getBill_id() : null)
                .deliveryManId(deliveryman != null ? deliveryman.getId() : null)
                .deliveryManName(deliveryman != null ? deliveryman.getDeliveryman_name() : "Not assigned")
                .deliveryManPhone(deliveryman != null ? deliveryman.getDeliveryman_phone() : "-")
                .build();
    }


    // Create Order from Request Bean
//    @Transactional
//    public OrderResponseBean createOrder(OrderRequestBean request) {
//
//        // Fetch customer
//        FoodCustomer customer = foodCustomerRepository.findById(request.getCustomerId())
//                .orElseThrow(() -> new ConstraintValidationException("Error","Customer not found"));
//
//        // Fetch hotel
//        Hotel hotel = hotelRepository.findById(request.getHotelId())
//                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel not found"));
//
//        // Fetch foods by name and hotel
//        List<Food> foods = foodRepository.findByFoodNameInAndHotelId(request.getFoodNames(), hotel.getId());
//        if (foods.isEmpty()) {
//            throw new ConstraintValidationException("Error","No valid food items found in this hotel");
//        }
//
//        // Check for missing foods
//        List<String> foundFoodNames = foods.stream().map(Food::getFoodName).toList();
//        List<String> missingFoods = request.getFoodNames().stream()
//                .filter(name -> !foundFoodNames.contains(name))
//                .toList();
//        if (!missingFoods.isEmpty()) {
//            throw new ConstraintValidationException("Error","These foods are not available in the selected hotel: " + missingFoods);
//        }
//
//        // Calculate total price + GST
//        double totalPrice = foods.stream().mapToDouble(f -> f.getFoodPrice() != null ? f.getFoodPrice() : 0.0).sum();
//        double gst = totalPrice * 0.05;
//        double totalAmountWithGST = totalPrice + gst;
//
//        // Create bill
//        Bill bill = new Bill();
//        bill.setCustomerName(customer.getCustomerName());
//        bill.setCustomerPhone(customer.getMobile_no());
//        bill.setTotalAmount(totalAmountWithGST);
//        bill.setGst(gst);
//        Bill savedBill = billRepository.save(bill);
//
//        // Create order
//        OrderFood order = new OrderFood();
//        order.setFoodcustomer(customer);
//        order.setHotel(hotel);
//        order.setFoods(foods);
//        order.setOrderStatus(OrderStatus.PENDING);
//        order.setTotal_price(totalPrice);
//        order.setBill(savedBill);
//        order.setCustomerName(customer.getCustomerName());
//        OrderFood savedOrder = orderFoodRepository.save(order);
//
//        // Assign delivery man
////        DeliverymanDetail deliveryMan = deliverymanDetailsRepository.findFirstByAvailableTrue().orElse(null);
////        if (deliveryMan != null) {
////            savedOrder.setDeliverymanDetail(deliveryMan);
////            savedOrder.setOrderStatus(OrderStatus.CONFIRMED);
////            deliveryMan.setAvailable(true); // Mark delivery man as assigned
////            deliverymanDetailsRepository.save(deliveryMan);
////            orderFoodRepository.save(savedOrder);
////        }
//
//        // Build response safely with null checks for deliveryman
//        OrderResponseBean.OrderResponseBeanBuilder responseBuilder = OrderResponseBean.builder()
//                .orderId(savedOrder.getId())
//                .orderStatus(savedOrder.getOrderStatus())
//                .customerId(customer.getId())
//                .customerName(customer.getCustomerName())
//                .customerPhone(customer.getMobile_no())
//                .hotelId(hotel.getId())
//                .hotelName(hotel.getHotel_name())
//                .hotelAddress(hotel.getHotel_address())
//                .foodNames(foundFoodNames)
//                .totalPrice(totalPrice)
//                .gst(gst)
//                .totalAmountWithGST(totalAmountWithGST)
//                .billId(savedBill.getBill_id());
//
////        if (deliveryMan != null) {
////            responseBuilder.deliveryManId(deliveryMan.getId())
////                    .deliveryManName(deliveryMan.getDeliveryman_name())
////                    .deliveryManPhone(deliveryMan.getDeliveryman_phone());
////        } else {
////            responseBuilder.deliveryManId(null)
////                    .deliveryManName(null)
////                    .deliveryManPhone(null);
////        }
//
//        return responseBuilder.build();
//    }


    // Get Order by ID
    public OrderResponseBean getOrderById(UUID orderId) {
        OrderFood order = orderFoodRepository.findById(orderId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Order not found"));
        return toResponse(order);
    }

    // Get all orders
    public PageImpl<OrderResponseBean> getAllOrders(Pageable pageable) {
        Page<OrderFood> pageResult = orderFoodRepository.findAll(pageable);

        List<OrderResponseBean> ordersList = pageResult.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(ordersList, pageable, pageResult.getTotalElements());
    }


    // Update Order Status
    public OrderResponseBean updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        OrderFood order = orderFoodRepository.findById(orderId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Order not found"));
        order.setOrderStatus(newStatus);
        return toResponse(orderFoodRepository.save(order));
    }

    // Delete Order
    public void deleteOrder(UUID orderId) {
        OrderFood order = orderFoodRepository.findById(orderId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Order not found"));
        orderFoodRepository.delete(order);
    }

    // Assign Delivery Man
//    public OrderResponseBean assignDeliveryMan(UUID orderId, UUID deliveryManId) {
//        OrderFood order = orderFoodRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        DeliverymanDetail deliveryMan = deliverymanDetailsRepository.findById(deliveryManId)
//                .orElseThrow(() -> new RuntimeException("Delivery man not found"));
//
//        order.setDeliverymanDetail(deliveryMan);
//        order.setOrderStatus(OrderStatus.CONFIRMED); // Auto-update status
//
//        return toResponse(orderFoodRepository.save(order));
//    }

    public OrderFood createOrderByFoodNames(UUID customerId, UUID hotelId, List<String> foodNames, String orderStatus) {
        // Fetch customer
        FoodCustomer customer = foodCustomerRepository.findById(customerId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Customer not found"));

        // Fetch hotel
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel not found"));

        // Fetch foods by name and hotel
        List<Food> foods = foodRepository.findByFoodNameInAndHotelId(foodNames, hotelId);

        if (foods.isEmpty()) {
            throw new ConstraintValidationException("Error","No valid food items found in this hotel");
        }

        // Optional: check for missing foods
        List<String> foundFoodNames = foods.stream().map(Food::getFoodName).toList();
        List<String> missingFoods = foodNames.stream()
                .filter(name -> !foundFoodNames.contains(name))
                .toList();
        if (!missingFoods.isEmpty()) {
            throw new ConstraintValidationException("Error","These foods are not available in the selected hotel: " + missingFoods);
        }

        // Calculate total price + GST
        Double totalPrice = foods.stream().mapToDouble(f -> f.getFoodPrice() != null ? f.getFoodPrice() : 0.0).sum();
        Double gst = totalPrice * 0.05;
        Double totalAmountWithGST = totalPrice + gst;

        // Create bill
        Bill bill = new Bill();
        bill.setCustomerPhone(customer.getMobile_no());
        bill.setCustomerName(customer.getCustomerName());
        bill.setTotalAmount(totalAmountWithGST);
        bill.setGst(gst);
        Bill savedBill = billRepository.save(bill);

        // Convert orderStatus String to Enum
        OrderStatus status = OrderStatus.valueOf(orderStatus.toUpperCase());

        // Create order
        OrderFood order = new OrderFood();
        order.setFoodcustomer(customer);
        order.setFoods(foods);
        order.setHotel(hotel);
        order.setOrderStatus(status);
        order.setTotal_price(totalPrice);
        order.setBill(savedBill);
        order.setCustomerName(customer.getCustomerName());

        return orderFoodRepository.save(order);
    }


    //    public OrderFood placeOrder(OrderFood order) {
//        Hotel hotel = hotelRepository.findById(order.getId())
//                .orElseThrow(() -> new RuntimeException("Hotel not found"));
//
//        FoodCustomer customer = customerRepository.findById(order.getId())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        List<DeliverymanDetail> availableDrivers = deliverymanDetailsRepository.findByStatus(DeliverymanStatus.AVAILABLE);
//
//        if (availableDrivers.isEmpty()) {
//            throw new RuntimeException("No available deliveryman");
//        }
//
//        Location deliveryLocation = customer.getLocation();
//
//        DeliverymanDetail nearest = availableDrivers.stream()
//                .min(Comparator.comparingDouble(dm ->
//                        DistanceUtils.calculateDistance(dm.getCurrentLocation(), deliveryLocation)
//                ))
//                .orElseThrow(() -> new RuntimeException("Error finding nearest deliveryman"));
//
//        OrderFood orders = OrderFood.builder()
//                .hotel(hotel)
//                 .customer(customer)
//                .deliveryLocation(deliveryLocation)
//                .deliveryman(nearest)
//                .status("ASSIGNED")
//                .build();
//
//        orderFoodRepository.save(order);
//
//        nearest.setStatus(DeliverymanStatus.BUSY);
//        deliverymanDetailsRepository.save(nearest);
//
//        double distance = DistanceUtils.calculateDistance(nearest.getCurrentLocation(), deliveryLocation);
//
//        // Fill response fields in the same DTO
//        order.setOrderId(order.getId());
//        order.setStatus(order.getStatus());
//        order.setHotelName(hotel.getHotelName());
//        order.setDeliverymanId(nearest.getId());
//        order.setDeliverymanName(nearest.getDeliverymanName());
//        order.setDeliverymanDistance(distance);
//
//        return orderDto;
//    }
    public OrderFood assignDeliverymanToOrder(UUID orderId, UUID id) {
        // Fetch the order
        OrderFood order = orderFoodRepository.findById(orderId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Order not found"));

        // Fetch the deliveryman
        DeliverymanDetail deliveryman = deliverymanDetailsRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Deliveryman not found"));

        // Check if deliveryman is available
        if (!deliveryman.isAvailable()) {
            throw new ConstraintValidationException("Error","Deliveryman is not available");
        }

        // Assign deliveryman to order
        order.setDeliverymanDetail(deliveryman);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        // Link order in deliveryman entity
        deliveryman.setOrderFood(order);
        deliveryman.setAvailable(false);   // Mark as assigned
        deliveryman.setStatus(DeliverymanStatus.ASSIGNED);

        // Save both entities
        deliverymanDetailsRepository.save(deliveryman);
        return orderFoodRepository.save(order);
    }

    // Method to calculate distance between two points (Haversine formula)
//    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
//        final int R = 6371; // Radius of earth in km
//        double latDistance = Math.toRadians(lat2 - lat1);
//        double lonDistance = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return R * c; // distance in km
//    }

    public OrderFood assignNearestDeliveryman(UUID orderId) {
        OrderFood order = orderFoodRepository.findById(orderId)
                .orElseThrow(() -> new ConstraintValidationException("error","Order not found"));

        // Ensure hotel is set and has a location
        if (order.getHotel() == null || order.getHotel().getLocation() == null) {
            throw new ConstraintValidationException("Error","Hotel or hotel location is not set for this order");
        }

        // Get all deliverymen (ignoring availability for now)
        List<DeliverymanDetail> deliverymen = deliverymanDetailsRepository.findAll();
        if (deliverymen.isEmpty()) {
            throw new ConstraintValidationException("error","No deliveryman found");
        }

        // Find the nearest deliveryman
        DeliverymanDetail nearest = deliverymen.stream()
                .min(Comparator.comparing(d ->
                        calculateDistance(
                                order.getHotel().getLocation().getLatitude(),
                                order.getHotel().getLocation().getLongitude(),
                                d.getLocation().getLatitude(),
                                d.getLocation().getLongitude()
                        ))).get();

        // Assign deliveryman to order
        order.setDeliverymanDetail(nearest);

        // Update order status
        order.setOrderStatus(OrderStatus.CONFIRMED);

        // Save order
        return orderFoodRepository.save(order);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula or simple Euclidean distance
        double dx = lat1 - lat2;
        double dy = lon1 - lon2;
        return Math.sqrt(dx * dx + dy * dy);
    }
//    @Transactional
//    public List<OrderFood> placeMultipleOrders(List<OrderRequestBean> orderRequests) {
//        List<OrderFood> orders = new ArrayList<>();
//
//        for (OrderRequestBean request : orderRequests) {
//            Hotel hotel = hotelRepository.findById(request.getHotelId())
//                    .orElseThrow(() -> new RuntimeException("Hotel not found"));
//
//            List<Food> foods = foodRepository.findByFoodNameInAndHotelId(
//                    request.getFoodNames(), request.getHotelId()
//            );
//
//            OrderFood order = OrderFood.builder()
//                    .hotel(hotel)
//                    .foods(foods)
//                    .orderStatus(OrderStatus.valueOf(request.getOrderStatus()))
//                    .build();
//
//            orders.add(orderFoodRepository.save(order));
//        }
//
//        return orders;
//    }
    public OrderResponseBean createOrder(UUID customerId, OrderRequestBean request) {
        FoodCustomer customer = foodCustomerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        List<Food> foods = foodRepository.findByFoodNameInAndHotelId(request.getFoodNames(), hotel.getId());
        if(foods.isEmpty()){
            throw new RuntimeException("No valid food items found in this hotel");
        }

        List<String> foundFoodNames = foods.stream().map(Food::getFoodName).toList();
        List<String> missingFoods = request.getFoodNames().stream()
                .filter(name -> !foundFoodNames.contains(name)).toList();
        if(!missingFoods.isEmpty()){
            throw new RuntimeException("Foods not available: " + missingFoods);
        }

        // Calculate total price + GST
        double totalPrice = foods.stream().mapToDouble(f -> f.getFoodPrice() != null ? f.getFoodPrice() : 0.0).sum();
        double gst = totalPrice * 0.05;
        double totalAmountWithGST = totalPrice + gst;

        // Create Bill
        Bill bill = new Bill();
        bill.setCustomerName(customer.getCustomerName());
        bill.setCustomerPhone(customer.getMobile_no());
        bill.setTotalAmount(totalAmountWithGST);
        bill.setGst(gst);
        bill.setPaymentMode(request.getPaymentMode());
        bill.setPaid(true); // Payment done immediately
        Bill savedBill = billRepository.save(bill);

        // Create Order
        OrderFood order = new OrderFood();
        order.setFoodcustomer(customer);
        order.setHotel(hotel);
        order.setFoods(foods);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotal_price(totalPrice);
        order.setBill(savedBill);
        order.setCustomerName(customer.getCustomerName());

        // Assign nearest available deliveryman
        List<DeliverymanDetail> availableDeliverymen = deliverymanDetailsRepository.findByStatus(DeliverymanStatus.AVAILABLE);
        DeliverymanDetail assignedDeliveryman = availableDeliverymen.isEmpty() ? null : availableDeliverymen.get(0);
        if(assignedDeliveryman != null){
            order.setDeliverymanDetail(assignedDeliveryman);
            assignedDeliveryman.setStatus(DeliverymanStatus.BUSY);
            deliverymanDetailsRepository.save(assignedDeliveryman);
        }

        OrderFood savedOrder = orderFoodRepository.save(order);

        // Build Response
        OrderResponseBean.OrderResponseBeanBuilder responseBuilder = OrderResponseBean.builder()
                .orderId(savedOrder.getId())
                .orderStatus(savedOrder.getOrderStatus())
                .customerId(customer.getId())
                .customerName(customer.getCustomerName())
                .customerPhone(customer.getMobile_no())
                .hotelId(hotel.getId())
                .hotelName(hotel.getHotel_name())
                .hotelAddress(hotel.getHotel_address())
                .foodNames(foundFoodNames)
                .totalPrice(totalPrice)
                .gst(gst)
                .totalAmountWithGST(totalAmountWithGST)
                .billId(savedBill.getBill_id())
                .paymentMode(savedBill.getPaymentMode())
                .paid(savedBill.isPaid());

        if(assignedDeliveryman != null){
            responseBuilder.deliveryManId(assignedDeliveryman.getId())
                    .deliveryManName(assignedDeliveryman.getDeliveryman_name())
                    .deliveryManPhone(assignedDeliveryman.getDeliveryman_phone());
        }

        return responseBuilder.build();
    }

    // Cancel Order
    public OrderFood cancelOrder(UUID orderId) {
        OrderFood order = orderFoodRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(order.getOrderStatus() == OrderStatus.DELIVERED){
            throw new RuntimeException("Cannot cancel a delivered order");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        if(order.getDeliverymanDetail() != null){
            order.getDeliverymanDetail().setStatus(DeliverymanStatus.AVAILABLE);
            deliverymanDetailsRepository.save(order.getDeliverymanDetail());
        }

        if(order.getBill() != null && order.getBill().isPaid()){
            // mark refunded
            order.getBill().setPaid(false);
            billRepository.save(order.getBill());
        }

        return orderFoodRepository.save(order);
    }

    // Mark Delivered
    public OrderFood markDelivered(UUID orderId){
        OrderFood order = orderFoodRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(OrderStatus.DELIVERED);

        if(order.getDeliverymanDetail() != null){
            order.getDeliverymanDetail().setStatus(DeliverymanStatus.AVAILABLE);
            deliverymanDetailsRepository.save(order.getDeliverymanDetail());
        }

        return orderFoodRepository.save(order);
    }
    public List<OrderFood> filterOrders(
            String orderStatus,      // e.g., "PENDING"
            String customerName,     // Customer name
            UUID hotelId,            // Hotel ID
            String foodName,         // Food name
            Double minPrice,         // Minimum total price
            Double maxPrice          // Maximum total price
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderFood> cq = cb.createQuery(OrderFood.class);
        Root<OrderFood> orderRoot = cq.from(OrderFood.class);

        List<Predicate> predicates = new ArrayList<>();

        // Join relations
        Join<OrderFood, FoodCustomer> customerJoin = orderRoot.join("foodcustomer", JoinType.LEFT);
        Join<OrderFood, Hotel> hotelJoin = orderRoot.join("hotel", JoinType.LEFT);
        Join<OrderFood, Food> foodJoin = orderRoot.join("foods", JoinType.LEFT);

        // Filter by orderStatus
        if (orderStatus != null && !orderStatus.isEmpty()) {
            predicates.add(cb.equal(orderRoot.get("orderStatus"), OrderStatus.valueOf(orderStatus.toUpperCase())));
        }

        // Filter by customerName
        if (customerName != null && !customerName.isEmpty()) {
            predicates.add(cb.like(cb.lower(customerJoin.get("customerName")), "%" + customerName.toLowerCase() + "%"));
        }

        // Filter by hotelId
        if (hotelId != null) {
            predicates.add(cb.equal(hotelJoin.get("id"), hotelId));
        }

        // Filter by foodName
        if (foodName != null && !foodName.isEmpty()) {
            predicates.add(cb.like(cb.lower(foodJoin.get("foodName")), "%" + foodName.toLowerCase() + "%"));
        }

        // Filter by price range
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(orderRoot.get("total_price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(orderRoot.get("total_price"), maxPrice));
        }

        // Build query
        cq.select(orderRoot).distinct(true); // prevent duplicates due to joins
        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}


