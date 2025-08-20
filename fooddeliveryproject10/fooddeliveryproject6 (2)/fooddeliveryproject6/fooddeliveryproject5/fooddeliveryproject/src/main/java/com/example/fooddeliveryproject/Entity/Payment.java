package com.example.fooddeliveryproject.Entity;

import com.example.fooddeliveryproject.Enum.PaymentMode;
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
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
   //@JoinColumn(name = "bill_id", referencedColumnName = "Bill_id")
    @JsonManagedReference
    private Bill bill;
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;
    private String paymentStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id != null && id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
