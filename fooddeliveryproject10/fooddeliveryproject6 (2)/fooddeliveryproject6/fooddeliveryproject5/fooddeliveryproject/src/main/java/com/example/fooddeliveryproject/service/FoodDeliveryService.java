package com.example.fooddeliveryproject.service;

import com.example.ConstantClass;
import com.example.ConstantMesage.ErrorResponse;
import com.example.Mapper.MapperUtils;
import com.example.RequestBean.LocationDto;
import com.example.RequestBean.OrderDto;
import com.example.fooddeliveryproject.Enum.PaymentMode;
import com.example.fooddeliveryproject.ExceptionHandling.CustomerNotFoundException;
import com.example.fooddeliveryproject.RequestBean.*;
import com.example.fooddeliveryproject.Entity.*;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.ResponseBean.CustomerResponseBean;
import com.example.fooddeliveryproject.ResponseBean.OrderResponseBean;
import com.example.fooddeliveryproject.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodDeliveryService {
    @Autowired
    private OrderFoodService orderFoodService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private HotelRatingRepository hotelRatingRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private DeliverymanRatingRepository deliverymanRatingRepository;
    @Autowired
    private DeliverymanDetailsRepository deliverymanDetailsRepository;
    @Autowired
    private BillRepository billRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public PageImpl<CustomerResponseBean> getAllHotels(Pageable pageable) {
        Page<FoodCustomer> pageResult = customerRepository.findAll(pageable);

        List<CustomerResponseBean> hostelList = pageResult.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(hostelList, pageable, pageResult.getTotalElements()) ;
    }
    public CustomerResponseBean getCustomerById(UUID id) {
        try {
            if(id == null) {
                throw new  CustomerNotFoundException("error","provide the customer id");
            }
            if (customerRepository.existsById(id)) {
                FoodCustomer entity = customerRepository.findById(id).orElseThrow();
                return convertToResponseDto(entity);
            }
        }catch (ConstraintValidationException e){
            throw new ConstraintValidationException("error","Customer not found");
        }
        return null;
    }
    public ErrorResponse addData(CustomerRequestBean requestDto) {
        try {
            FoodCustomer entity = convertToEntity(requestDto);
            customerRepository.save(entity);
            return new ErrorResponse(ConstantClass.SUCCESS,ConstantClass.INSERTED);
        } catch (DataIntegrityViolationException e) {
        return new ErrorResponse( ConstantClass.STATUS,
        ConstantClass.CONSTRAINT_VIOLATION);
    }catch (EntityNotFoundException e) {
            return new ErrorResponse(ConstantClass.STATUS,ConstantClass.MISSING_DATA);

    }
    }
    public ErrorResponse saveAllData(List<CustomerRequestBean> customerDtos) {
        if(customerDtos == null || customerDtos.isEmpty()) {
            return new ErrorResponse(ConstantClass.STATUS,ConstantClass.EMPTY) ;
        }
        List<FoodCustomer> hostelEntities = customerDtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        customerRepository.saveAll(hostelEntities);
        return new ErrorResponse(ConstantClass.SUCCESS,ConstantClass.INSERTED);
    }

    public String updateFood(UUID id, CustomerRequestBean requestDto) {
       if(foodRepository.existsById(id)) {
           FoodCustomer existing = customerRepository.findById(id).get();


           FoodCustomer updated = convertToEntity(requestDto);
           existing.setCustomerName(updated.getCustomerName());
           existing.setEmail(updated.getEmail());
           existing.setMobile_no(updated.getMobile_no());
           existing.setAddress(updated.getAddress());
           customerRepository.save(existing);
           return ConstantClass.SUCCESS;
       }
       return ConstantClass.FAILED;
    }

    public String deleteFood(UUID id) {
        try{
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return ConstantClass.DELETED;
        }
    }catch(ObjectNotFoundException e) {
            return ConstantClass.OBJECTNOTFOUND;
        }
        return ConstantClass.FAILED;
    }

    public String deleteAllFood() {
        try {
            if(customerRepository.findAll().isEmpty()) {
                return ConstantClass.MISSING_DATA;
            }
            else{
                customerRepository.deleteAll();
                return "All Details deleted";
            }
        }catch (Exception e) {
            return ConstantClass.EMPTY;
        }
    }
    private FoodCustomer convertToEntity(CustomerRequestBean dto) {
        Location address = Location.builder()
                .id(dto.getLocation() != null ? dto.getLocation().getId(): null)
                .location_name(dto.getLocation() != null ? dto.getLocation().getLocation_name() : null)
                .build();
        FoodCustomer customer = FoodCustomer.builder()
                .customerName(dto.getCustomerName())
                .email(dto.getEmail())
                .mobile_no(dto.getMobile_no())
                .address(dto.getAddress())
                .location(address)
                .build();

        return customer;

    }

    private CustomerResponseBean convertToResponseDto(FoodCustomer entity) {
        LocationDto addressDto = LocationDto.builder()
                .id(entity.getLocation() != null ? entity.getLocation().getId()  : null)
                .location_name(entity.getLocation() != null ? entity.getLocation().getLocation_name() : null)
            .build();
       List<OrderResponseBean> ordersDto = entity.getOrders().stream()
               .map(order->OrderResponseBean.builder()
                       .orderId(order.getId())
                       .orderStatus(order.getOrderStatus())
                       .totalPrice(order.getTotal_price())
                       .hotelName(order.getHotel()!=null?order.getHotel().getHotel_name():null)
                       .foodNames(order.getFoods()!=null?order.getFoods().stream().map(Food::getFoodName).toList():new ArrayList<>()).build()).toList();

        return CustomerResponseBean.builder()
                .id(entity.getId())
                .customerName(entity.getCustomerName())
                .email(entity.getEmail())
                .mobile_no(entity.getMobile_no())
                .address(entity.getAddress())
                .location(addressDto)
                .orders(ordersDto)
                .build();

    }
//    public DeliverymanRatingDto addDeliveryRating(DeliverymanRatingDto dto) {
//        DeliverymanDetail delivery = deliverymanDetailsRepository.findById(dto.getId())
//                .orElseThrow(() -> new RuntimeException("Hotel not found with ID: " + dto.getId()));
//
//        DeliverymanRating deliveryRating = DeliverymanRating.builder()
//                .rating(dto.getRating())
//                .feedback(dto.getFeedback())
//                .build();
//
//        delivery.setDe(deliveryRating);// use correct field name
//        deliveryRating.setDeliverymanDetail(delivery); // set the back-reference
//        deliverymanDetailsRepository.save(delivery);
//        return DeliverymanRatingDto.builder()
//                .id(delivery.getId())
//                .id(deliveryRating.getId())
//                .rating(deliveryRating.getRating())
//                .feedback(deliveryRating.getFeedback())
//                .build();
//    }
//    public DeliverymanRatingDto getDeliverymanRating(UUID id) {
//        try {
//
//            if(deliverymanRatingRepository.existsById(id)) {
//                throw new ConstraintValidationException("error", "Deliveryman already exists");
//            }
//            DeliverymanDetail delivery = deliverymanDetailsRepository.findById(id).get();
//
//            DeliverymanRating rating = delivery.getDeliveryman_rating();
//
//            if (rating == null) {
//                throw new ConstraintValidationException("error", "No rating found for this hotel");
//            }
//
//            return DeliverymanRatingDto.builder()
//                    .id(delivery.getId())
//                    .rating(rating.getRating())
//                    .feedback(rating.getFeedback())
//                    .build();
//        }catch (ConstraintValidationException e) {
//            throw new ConstraintValidationException("error","For one hotel one rating is allowed");
//        }
//    }

//    public PaymentDto getPayment(UUID billId) {
//        try {
//            if(paymentRepository.existsById(billId)) {
//                throw new ConstraintValidationException("error", "Payment already exists");
//            }
//            Bill bill = billRepository.findById(billId).get();
//
//            Payment payment = bill.getPayment();
//
//            if (payment == null) {
//                throw new ConstraintValidationException("error", "No BillId found for this hotel");
//            }
//            return PaymentDto.builder()
//                    .billId(bill.getBill_id())
//                    .paymentMode(payment.getPaymentMode())
//                    .paymentStatus(payment.getPaymentStatus())
//                    .build();
//        }catch (ConstraintValidationException e) {
//            throw new ConstraintValidationException("error","For one Bill one Payment is allowed");
//        }
//    }

//    public PaymentDto addPayment(@Valid PaymentDto dto) {
//        Bill bill = billRepository.findById(dto.getBillId())
//
//                .orElseThrow(() -> new RuntimeException("Hotel not found with ID: " + dto.getBillId()));
//        String status = (dto.getPaymentMode() != null) ? "Success" : "Failed";
//        Payment payment = Payment.builder()
//                .bill(bill)
//                .paymentMode(dto.getPaymentMode())
//                .paymentStatus(status)
//                .build();
//
//        bill.setPayment(payment);// use correct field name
//        payment.setBill(bill); // set the back-reference
//        billRepository.save(bill);
//        return PaymentDto.builder()
//                .billId(bill.getBill_id())
//                .paymentMode(payment.getPaymentMode())
//                .paymentStatus(payment.getPaymentStatus())
//                .build();
//    }

//    public BillDto getBill(UUID id) {
//        try {
//            OrderFood order = orderRepository.findById(id).get();
//
//
//            Bill bill = order.getBill();
//
//            if (bill == null) {
//                throw new ConstraintValidationException("error", "No rating found for this hotel");
//            }
//
//            return BillDto.builder()
//                    .id(order.getId())
//                    .customerName(bill.getCustomerName())
//                    .customerPhone(bill.getCustomerPhone())
//                    .gst(bill.getGst())
//                    .totalAmount(bill.getTotalAmount())
//                    .build();
//        }catch (ConstraintValidationException e) {
//            throw new ConstraintValidationException("error","For one hotel one rating is allowed");
//        }
//
//    }
    public List<Bill> findByPaymentmode(String paymentstatus) {
        return billRepository.findByPaymentstatus(paymentstatus);
    }
    public List<Hotel> filterHotels(Integer minOrders, Integer minFoods, Double minRating, Integer minDeliveries) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Hotel> cq = cb.createQuery(Hotel.class);
            Root<Hotel> hotelRoot = cq.from(Hotel.class);

            List<Predicate> wherePredicates = new ArrayList<>();
            List<Predicate> havingPredicates = new ArrayList<>();


            Join<Hotel, OrderFood> ordersJoin = null;
            Join<Hotel, Food> foodsJoin = null;
            Join<Hotel, Delivery> deliveryJoin = null;


            if (minRating != null) {
                wherePredicates.add(
                        cb.greaterThanOrEqualTo(hotelRoot.get("hotel_rating").get("rating"), minRating)
                );
            }


            if (minOrders != null) {
                ordersJoin = hotelRoot.join("orders", JoinType.LEFT);
                havingPredicates.add(cb.greaterThanOrEqualTo(cb.count(ordersJoin), minOrders.longValue()));
            }


            if (minFoods != null) {
                foodsJoin = hotelRoot.join("foods", JoinType.LEFT);
                havingPredicates.add(cb.greaterThanOrEqualTo(cb.count(foodsJoin), minFoods.longValue()));
            }


            if (minDeliveries != null) {
                deliveryJoin = hotelRoot.join("deliveries", JoinType.LEFT);
                havingPredicates.add(cb.greaterThanOrEqualTo(cb.count(deliveryJoin), minDeliveries.longValue()));
            }


            cq.select(hotelRoot);

            if (!wherePredicates.isEmpty()) {
                cq.where(wherePredicates.toArray(new Predicate[0]));
            }


            if (!havingPredicates.isEmpty()) {
                cq.groupBy(hotelRoot.get("id"));
                cq.having(havingPredicates.toArray(new Predicate[0]));
            }

            return entityManager.createQuery(cq).getResultList();
        }catch (ConstraintValidationException e) {
            throw new ConstraintValidationException("error","Customer is not present");
        }
    }
    public List<OrderFood> getOrdersByCustomerName(String customerName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderFood> cq = cb.createQuery(OrderFood.class);
        Root<OrderFood> orderRoot = cq.from(OrderFood.class);
        Join<OrderFood, FoodCustomer> customerJoin = orderRoot.join("foodcustomer");
        Predicate customerNamePredicate = cb.equal(customerJoin.get("customerName"), customerName);
        cq.where(customerNamePredicate);
        return entityManager.createQuery(cq).getResultList();
    }
    @Transactional
    public List<Bill> filterBillsByPaymentMode(PaymentMode paymentMode) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Bill> cq = cb.createQuery(Bill.class);
        Root<Bill> billRoot = cq.from(Bill.class);
        Join<Bill, Payment> paymentJoin = billRoot.join("payment", JoinType.INNER);
        Predicate predicate = cb.equal(paymentJoin.get("paymentMode"), paymentMode);cq.select(billRoot).where(predicate);
        return entityManager.createQuery(cq).getResultList();
    }
    public List<FoodCustomer> filterCustomers(
            String customerName,
            String email,
            String hotel_name,
            String foodName,
            String locationName)
    {
//    {
//            if ((customerName == null || customerName.isBlank()) &&
//                    (email == null || email.isBlank()) &&
//                    (hotel_name == null || hotel_name.isBlank()) &&
//                    (foodName == null || foodName.isBlank()) &&
//                    (locationName == null || locationName.isBlank())) {
//                throw new IllegalArgumentException("At least one filter parameter must be provided");
//            }
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<FoodCustomer> cq = cb.createQuery(FoodCustomer.class);
            Root<FoodCustomer> root = cq.from(FoodCustomer.class);

            List<Predicate> predicates = new ArrayList<>();

            if (customerName != null && !customerName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("customerName")), "%" + customerName.toLowerCase() + "%"));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("email")), email.toLowerCase()));
            }
            if (hotel_name != null && !hotel_name.isBlank()) {
                Join<FoodCustomer, Hotel> hotelJoin = root.join("hotels", JoinType.INNER);
                predicates.add(cb.like(cb.lower(hotelJoin.get("hotel_name")), "%" + hotel_name.toLowerCase() + "%"));
            }
            if (foodName != null && !foodName.isBlank()) {
                Join<FoodCustomer, Food> foodJoin = root.join("food", JoinType.INNER);
                predicates.add(cb.like(cb.lower(foodJoin.get("foodName")), "%" + foodName.toLowerCase() + "%"));
            }
            if (locationName != null && !locationName.isBlank()) {
                Join<FoodCustomer, Location> locJoin = root.join("location", JoinType.INNER);
                predicates.add(cb.like(cb.lower(locJoin.get("location_name")), "%" + locationName.toLowerCase() + "%"));
            }

            cq.distinct(true);
            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            return entityManager.createQuery(cq).getResultList();
        } catch (PersistenceException e) {
            // JPA query problem
            throw new RuntimeException("Database query error: " + e.getMessage(), e);
        } catch (ConstraintValidationException e) {
            throw new ConstraintValidationException("error", "Customer is not present");
        }catch (MethodArgumentTypeMismatchException me){
            throw  new RuntimeException("url is wrong");
        }
    }
    public List<Food> filterFood(UUID id, String hotelName, String foodName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Food> cq = cb.createQuery(Food.class);
        Root<Food> foodRoot = cq.from(Food.class);

        Join<Food, FoodCustomer> customerJoin = foodRoot.join("foodcustomer", JoinType.LEFT);
        Join<Food, Hotel> hotelJoin = foodRoot.join("hotel", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (id != null) {
            predicates.add(cb.equal(customerJoin.get("id"), id));
        }
        if (hotelName != null && !hotelName.isEmpty()) {
            predicates.add(cb.equal(hotelJoin.get("hotel_name"), hotelName));
        }
        if (foodName != null && !foodName.isEmpty()) {
            predicates.add(cb.equal(foodRoot.get("foodName"), foodName));
        }

        cq.select(foodRoot).where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq).getResultList();
    }
    public List<OrderDto> filterOrders(
            String orderStatus,
            String customerName,
            Double minPrice,
            Double maxPrice,
            String foodName
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderFood> cq = cb.createQuery(OrderFood.class);
        Root<OrderFood> orderRoot = cq.from(OrderFood.class);
        List<Predicate> predicates = new ArrayList<>();
        if (orderStatus != null && !orderStatus.isEmpty()) {
            predicates.add(cb.like(cb.lower(orderRoot.get("orderStatus")), "%" + orderStatus.toLowerCase() + "%"));
        }
        if (customerName != null && !customerName.isEmpty()) {
            Join<Order, FoodCustomer> customerJoin = orderRoot.join("foodcustomer", JoinType.INNER);
            predicates.add(cb.like(cb.lower(customerJoin.get("customerName")), "%" + customerName.toLowerCase() + "%"));
        }
        if (minPrice != null) {
            predicates.add(cb.ge(orderRoot.get("total_price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.le(orderRoot.get("total_price"), maxPrice));
        }
        if (foodName != null && !foodName.isEmpty()) {
            Join<Order, Food> foodJoin = orderRoot.join("foods", JoinType.INNER);
            predicates.add(cb.like(cb.lower(foodJoin.get("foodName")), "%" + foodName.toLowerCase() + "%"));
        }
        cq.select(orderRoot).distinct(true);
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));}
        List<OrderFood> orders = entityManager.createQuery(cq).getResultList();
        // Convert List<Order> to List<OrderDto> (write a mapper function)
        return MapperUtils.mapList(orders, OrderDto.class);
    }
    public OrderFood createOrder(UUID customerId, List<UUID> foodIds, String orderStatus) {
        // Step 1: Get customer
        FoodCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Step 2: Get foods
        List<Food> foods = foodRepository.findAllById(foodIds);

        // Step 3: Calculate total price
        double totalPrice = foods.stream()
                .mapToDouble(Food::getFoodPrice)
                .sum();

        // Step 4: Create order
        OrderFood order = OrderFood.builder()
                .customerName(customer.getCustomerName())
                .foodcustomer(customer)
                .foods(foods)
                .total_price(totalPrice)
                .build();

        // Step 5: Save
        return orderRepository.save(order);
    }}

//    public List<DeliverymanRatingBean> getAllDeliverymanRatings() {
//            return deliverymanRatingRepository.findAll()
//                    .stream()
//                    .map(rating -> DeliverymanRatingDto.builder()
//                            .id(rating.getId())
//                            .rating(rating.getRating())
//                            .feedback(rating.getFeedback())
//                            .build())
//                    .toList();
//        }
//    }





