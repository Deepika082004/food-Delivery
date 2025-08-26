package com.example.fooddeliveryproject.controller;


import com.example.fooddeliveryproject.Entity.Hotel;
import com.example.fooddeliveryproject.Entity.Hotel_Rating;
import com.example.RequestBean.HotelRatingDto;
import com.example.fooddeliveryproject.RequestBean.HotelRequestBean;
import com.example.fooddeliveryproject.ResponseBean.HotelResponseBean;
import com.example.fooddeliveryproject.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<PageImpl<HotelResponseBean>> getAllHotels(Pageable pageable) {
        PageImpl<HotelResponseBean> hotels = hotelService.getAllHotels(pageable);
        return ResponseEntity.ok(hotels);
    }

    // Get hotel by ID
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseBean> getHotelById(@PathVariable UUID id) {
        HotelResponseBean hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    // Create a new hotel
    @PostMapping
    public ResponseEntity<HotelResponseBean> createHotel(@Valid @RequestBody HotelRequestBean request) {
        HotelResponseBean createdHotel = hotelService.createHotel(request);
        return ResponseEntity.ok(createdHotel);
    }

    // Create multiple hotels in bulk
    @PostMapping("/bulk")
    public ResponseEntity<List<HotelResponseBean>> createHotels(@Valid @RequestBody List<HotelRequestBean> requests) {
        List<HotelResponseBean> createdHotels = hotelService.createHotels(requests);
        return ResponseEntity.ok(createdHotels);
    }

    // Update an existing hotel
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseBean> updateHotel(
            @PathVariable UUID id,
            @RequestBody HotelRequestBean request) {
        HotelResponseBean updatedHotel = hotelService.updateHotel(id, request);
        return ResponseEntity.ok(updatedHotel);
    }

    // Delete hotel by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable UUID id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    // Delete all hotels
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllHotels() {
        String msg = hotelService.deleteAllHotel();
        return ResponseEntity.ok(msg);
    }
    @PostMapping("/{id}")
    public ResponseEntity<Hotel_Rating> addRating( @Valid
            @PathVariable UUID id,
            @RequestBody HotelRatingDto dto) {
        Hotel_Rating rating = hotelService.addRating(id, dto);
        return ResponseEntity.ok(rating);
    }
//    @GetMapping("/filter")
//    public ResponseEntity<List<Hotel>> filterHotels(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String nameMatchType,
//            @RequestParam(required = false) String phone,
//            @RequestParam(required = false) String address,
//            @RequestParam(required = false) Boolean hasOrders,
//            @RequestParam(required = false) Boolean hasRatings,
//            @RequestParam(required = false) Boolean hasFoods,
//            @RequestParam(defaultValue = "hotel_name") String sortBy,
//            @RequestParam(defaultValue = "asc") String sortDir,
//            @RequestParam(defaultValue = "0") Integer page,
//            @RequestParam(defaultValue = "10") Integer size
//    ) {
//        return ResponseEntity.ok(
//                hotelService.filterHotels(
//                        name, nameMatchType, phone, address,
//                        hasOrders, hasRatings, hasFoods,
//                        sortBy, sortDir, page, size
//                )
//        );
//    }
@GetMapping("/by-location")
public ResponseEntity<List<Hotel>> getHotelsByLocation(@RequestParam String hotel_address) {
    return ResponseEntity.ok(hotelService.getHotelsByLocation(hotel_address));
}
    @GetMapping("/filter")
    public List<Hotel> filterHotels(
            @RequestParam(required = false) String foodName,
            @RequestParam(required = false) String hotel_address,
            @RequestParam(required = false) Double minRating
    ) {
        return hotelService.filterHotels(foodName, hotel_address, minRating);
    }
    @GetMapping("/hotels/nearby")
    public List<HotelResponseBean> getNearbyHotels(
            @RequestParam double customerLat,
            @RequestParam double customerLng,
            @RequestParam(defaultValue = "5") double maxDistanceKm) { // default 5 km radius
        return hotelService.getNearestHotels(customerLat, customerLng, maxDistanceKm);
    }

}
