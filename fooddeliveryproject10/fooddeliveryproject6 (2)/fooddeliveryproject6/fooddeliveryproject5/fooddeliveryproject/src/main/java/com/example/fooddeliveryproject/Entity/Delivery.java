package com.example.fooddeliveryproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID delivery_id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="food_delivery_id",referencedColumnName = "id")
    @JsonManagedReference
    private FoodCustomer foodcustomer;
    private String customer_name;
    private String mobile_phone;
    private String food_name;
    private String email;
    private String address;

    @OneToOne(mappedBy = "delivery", cascade = CascadeType.ALL)
    @JsonBackReference
    private DeliverymanDetail deliverymanDetail;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private FoodCustomer customer;   // Instead of duplicating name, phone, etc.

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderFood orderFood;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return delivery_id != null && delivery_id.equals(delivery.delivery_id);
    }

    @Override
    public int hashCode() {
        return delivery_id != null ? delivery_id.hashCode() : 0;
    }

}
