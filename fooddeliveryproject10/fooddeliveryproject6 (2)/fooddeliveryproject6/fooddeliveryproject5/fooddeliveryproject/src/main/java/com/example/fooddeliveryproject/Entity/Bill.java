package com.example.fooddeliveryproject.Entity;

import com.example.fooddeliveryproject.Enum.PaymentMode;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Bill_id;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name="Bill_id_pkey",referencedColumnName = "id")
    private Payment payment;
    private String CustomerName;
    private String CustomerPhone;
    private Double gst;
    private Double TotalAmount;
    private boolean paid;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customer_id")
    private FoodCustomer foodcustomer;
    @ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_bill",referencedColumnName = "id")
    private Hotel hotel;
    @OneToOne(mappedBy = "bill")
    @JsonBackReference
    private OrderFood order;
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

// true if payment done

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Bill_id != null && Bill_id.equals(bill.Bill_id);
    }
    @Override
    public int hashCode() {
        return Bill_id != null ? Bill_id.hashCode() : 0;
    }

}
