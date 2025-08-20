package com.example.fooddeliveryproject.Entity;

import com.example.fooddeliveryproject.Enum.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFood {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String customerName;
    private Double total_price;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @ManyToOne()
    @JoinColumn(name="food_customer_id")
    @JsonManagedReference
    private FoodCustomer foodcustomer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="orders_food",
            joinColumns = @JoinColumn(name="orderfood_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="food_id",referencedColumnName = "food_id")
    )
    @JsonManagedReference
    private List<Food> foods=new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="bill_id",referencedColumnName = "Bill_id")
    @JsonManagedReference
    private Bill bill;
    @OneToOne(mappedBy = "orderFood", cascade = CascadeType.ALL)
    private DeliverymanDetail deliverymanDetail;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderFood orderFood = (OrderFood) o;
        return id != null && id.equals(orderFood.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;

}}
