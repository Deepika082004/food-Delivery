package com.example.fooddeliveryproject.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliverymanRating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int rating;
    private String feedback;
    @OneToOne
    @JoinColumn(name="deliverymanRating",referencedColumnName = "id")
    @JsonManagedReference
    private DeliverymanDetail deliverymanDetail;
//    @ManyToOne
//    @JoinColumn(name = "deliveryman_id")
//    private DeliverymanDetail deliveryman;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private FoodCustomer customer;
    public boolean isPresent() {
        return deliverymanDetail != null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliverymanRating deliverymanRating = (DeliverymanRating) o;
        return id != null && id.equals(deliverymanRating.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
