package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Entity.Food;
import com.example.fooddeliveryproject.Entity.FoodCustomer;
import com.example.fooddeliveryproject.Entity.Hotel;
import com.example.fooddeliveryproject.Entity.Hotel_Rating;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.RequestBean.HotelRatingDto;
import com.example.fooddeliveryproject.RequestBean.HotelRequestBean;
import com.example.fooddeliveryproject.ResponseBean.HotelResponseBean;
import com.example.fooddeliveryproject.repository.CustomerRepository;
import com.example.fooddeliveryproject.repository.FoodRepository;
import com.example.fooddeliveryproject.repository.HotelRatingRepository;
import com.example.fooddeliveryproject.repository.HotelRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class HotelService {
    private final FoodService foodService;
    private final FoodRepository foodRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final HotelRatingRepository hotelRatingRepository;
    private final EntityManager entityManager;
    public PageImpl<HotelResponseBean> getAllHotels(Pageable pageable) {
        List<Hotel> hotels = hotelRepository.findAll();

        // Map entities to response DTOs
        List<HotelResponseBean> hotelResponses = hotels.stream().map(hotel -> {
            Double rating = hotelRatingRepository.getAverageRatingByHotelId(hotel.getId());
            if (rating == null) rating = 1.5;

            List<String> foodNames = hotel.getFood() != null
                    ? hotel.getFood().stream().map(f -> f.getFoodName()).toList()
                    : List.of();

            return HotelResponseBean.builder()
                    .id(hotel.getId())
                    .hotel_name(hotel.getHotel_name())
                    .hotel_phone(hotel.getHotel_phone())
                    .hotel_address(hotel.getHotel_address())
                    .averageRating(rating)
                    .foodNames(foodNames)
                    //.totalOrders(hotel.getOrders() != null ? hotel.getOrders().size() : 0)
                    .build();
        }).toList();

        // Apply pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), hotelResponses.size());
        List<HotelResponseBean> pagedList = hotelResponses.subList(start, end);

        return new PageImpl<>(pagedList, pageable, hotelResponses.size());
    }

    // Get hotel by ID
    public HotelResponseBean getHotelById(UUID id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel not found with ID: " + id));
        Double rating = hotelRatingRepository.getAverageRatingByHotelId(hotel.getId());
        if (rating == null) rating = 1.5;

        List<String> foodNames = hotel.getFood() != null
                ? hotel.getFood().stream().map(f -> f.getFoodName()).toList()
                : List.of();

        return HotelResponseBean.builder()
                .id(hotel.getId())
                .hotel_name(hotel.getHotel_name())
                .hotel_phone(hotel.getHotel_phone())
                .hotel_address(hotel.getHotel_address())
                .averageRating(rating)
                .foodNames(foodNames)
                //.totalOrders(hotel.getOrders() != null ? hotel.getOrders().size() : 0)
                .build();
    }

    // Create a single hotel
    public HotelResponseBean createHotel(HotelRequestBean request) {
        Hotel hotel = Hotel.builder()
                .hotel_name(request.getHotel_name())
                .hotel_phone(request.getHotel_phone())
                .hotel_address(request.getHotel_address())
                .location(request.getLocation())
                .build();

        Hotel savedHotel = hotelRepository.save(hotel);

        return HotelResponseBean.builder()
                .id(savedHotel.getId())
                .hotel_name(savedHotel.getHotel_name())
                .hotel_phone(savedHotel.getHotel_phone())
                .hotel_address(savedHotel.getHotel_address())
                .averageRating(1.5) // default rating
                .foodNames(List.of())
                //.totalOrders(hotel.getOrders() != null ? hotel.getOrders().size() : 0)
                .build();
    }

    // Create multiple hotels
    public List<HotelResponseBean> createHotels(List<HotelRequestBean> requests) {
        List<Hotel> hotels = requests.stream().map(req -> Hotel.builder()
                .hotel_name(req.getHotel_name())
                .hotel_phone(req.getHotel_phone())
                .hotel_address(req.getHotel_address())
                .location(req.getLocation())
                .build()).toList();

        List<Hotel> savedHotels = hotelRepository.saveAll(hotels);

        return savedHotels.stream().map(hotel -> HotelResponseBean.builder()
                .id(hotel.getId())
                .hotel_name(hotel.getHotel_name())
                .hotel_phone(hotel.getHotel_phone())
                .hotel_address(hotel.getHotel_address())
                .averageRating(1.5)
                .foodNames(List.of())
                //.totalOrders(0)
                .build()).toList();
    }

    // Update hotel
    public HotelResponseBean updateHotel(UUID id, HotelRequestBean request) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel not found with ID: " + id));

        existingHotel.setHotel_name(request.getHotel_name());
        existingHotel.setHotel_phone(request.getHotel_phone());
        existingHotel.setHotel_address(request.getHotel_address());

        Hotel updatedHotel = hotelRepository.save(existingHotel);

        Double rating = hotelRatingRepository.getAverageRatingByHotelId(updatedHotel.getId());
        if (rating == null) rating = 1.5;

        List<String> foodNames = updatedHotel.getFood() != null
                ? updatedHotel.getFood().stream().map(f -> f.getFoodName()).toList()
                : List.of();

        return HotelResponseBean.builder()
                .id(updatedHotel.getId())
                .hotel_name(updatedHotel.getHotel_name())
                .hotel_phone(updatedHotel.getHotel_phone())
                .hotel_address(updatedHotel.getHotel_address())
                .averageRating(rating)
                .foodNames(foodNames)
                //.totalOrders(updatedHotel.getOrders() != null ? updatedHotel.getOrders().size() : 0)
                .build();
    }

    // Delete hotel by ID
    public void deleteHotel(UUID id) {
        hotelRepository.deleteById(id);
    }

    // Delete all hotels
    public String deleteAllHotel() {
        hotelRepository.deleteAll();
        return "Hotel Deleted Successfully";
    }
    public Hotel_Rating addRating(UUID customerId, HotelRatingDto dto) {
        // Fetch hotel
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new ConstraintValidationException("Error","Hotel not found"));

        // Fetch customer
        FoodCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ConstraintValidationException("Error","Customer not found"));

        // Create rating
        Hotel_Rating rating = Hotel_Rating.builder()
                .hotel(hotel)
                .customer(customer)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        return hotelRatingRepository.save(rating);
    }
//    public List<Hotel> filterHotels(
//            String name,
//            String nameMatchType,
//            String phone,
//            String address,
//            Boolean hasOrders,
//            Boolean hasRatings,
//            Boolean hasFoods,
//            String sortBy,
//            String sortDir,
//            Integer page,
//            Integer size
//    ) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Hotel> cq = cb.createQuery(Hotel.class);
//        Root<Hotel> root = cq.from(Hotel.class);
//
//        List<Predicate> predicates = new ArrayList<>();
//
//        // 🔹 Hotel name
//        if (name != null && !name.isEmpty()) {
//            String lower = name.toLowerCase();
//            switch (nameMatchType == null ? "contains" : nameMatchType) {
//                case "exact" ->
//                        predicates.add(cb.equal(cb.lower(root.get("hotel_name")), lower));
//                case "startsWith" ->
//                        predicates.add(cb.like(cb.lower(root.get("hotel_name")), lower + "%"));
//                case "endsWith" ->
//                        predicates.add(cb.like(cb.lower(root.get("hotel_name")), "%" + lower));
//                default ->
//                        predicates.add(cb.like(cb.lower(root.get("hotel_name")), "%" + lower + "%"));
//            }
//        }
//
//        // 🔹 Phone
//        if (phone != null && !phone.isEmpty()) {
//            predicates.add(cb.like(cb.lower(root.get("hotel_phone")), "%" + phone.toLowerCase() + "%"));
//        }
//
//        // 🔹 Address
//        if (address != null && !address.isEmpty()) {
//            predicates.add(cb.like(cb.lower(root.get("hotel_address")), "%" + address.toLowerCase() + "%"));
//        }
//
//        // 🔹 Has Orders
//        if (hasOrders != null) {
//            if (hasOrders) {
//                predicates.add(cb.isNotEmpty(root.get("orders")));
//            } else {
//                predicates.add(cb.isEmpty(root.get("orders")));
//            }
//        }
//
//        // 🔹 Has Ratings
//        if (hasRatings != null) {
//            if (hasRatings) {
//                predicates.add(cb.isNotEmpty(root.get("hotel_rating")));
//            } else {
//                predicates.add(cb.isEmpty(root.get("hotel_rating")));
//            }
//        }
//
//        // 🔹 Has Foods
//        if (hasFoods != null) {
//            if (hasFoods) {
//                predicates.add(cb.isNotEmpty(root.get("food")));
//            } else {
//                predicates.add(cb.isEmpty(root.get("food")));
//            }
//        }
//
//        // Apply WHERE clause
//        cq.select(root).where(predicates.toArray(new Predicate[0]));
//
//        // 🔹 Sorting
//        if (sortBy != null && !sortBy.isEmpty()) {
//            if ("desc".equalsIgnoreCase(sortDir)) {
//                cq.orderBy(cb.desc(root.get(sortBy)));
//            } else {
//                cq.orderBy(cb.asc(root.get(sortBy)));
//            }
//        }
//
//        TypedQuery<Hotel> query = entityManager.createQuery(cq);
//
//        // 🔹 Pagination
//        if (page != null && size != null) {
//            query.setFirstResult(page * size);
//            query.setMaxResults(size);
//        }
//
//        return query.getResultList();
//    }
public List<Hotel> getHotelsByLocation(String hotel_address) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Hotel> cq = cb.createQuery(Hotel.class);

    Root<Hotel> hotel = cq.from(Hotel.class);

    cq.select(hotel)
            .where(cb.equal(cb.lower(hotel.get("hotel_address")), hotel_address.toLowerCase()));

    return entityManager.createQuery(cq).getResultList();
}
    public List<Hotel> filterHotels(String foodName, String hotel_address, Double minRating) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hotel> cq = cb.createQuery(Hotel.class);
        Root<Hotel> hotel = cq.from(Hotel.class);

        // Join with food
        Join<Hotel, Food> foodJoin = hotel.join("food", JoinType.LEFT);

        // Join with hotel_rating
        Join<Hotel, Hotel_Rating> ratingJoin = hotel.join("hotel_rating", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>(); // <-- make sure this is jakarta.persistence.criteria.Predicate

        if (foodName != null && !foodName.isEmpty()) {
            predicates.add(cb.equal(foodJoin.get("foodName"), foodName));
        }

        if (hotel_address != null && !hotel_address.isEmpty()) {
            predicates.add(cb.equal(hotel.get("hotel_address"), hotel_address));
        }

        if (minRating != null) {
            predicates.add(cb.greaterThanOrEqualTo(ratingJoin.get("rating"), minRating));
        }

        cq.select(hotel).distinct(true);
        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }

}
