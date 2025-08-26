package com.example.fooddeliveryproject.Entity;

import com.example.fooddeliveryproject.Enum.DeliverymanStatus;
import com.example.fooddeliveryproject.RequestBean.LocationCal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class DeliverymanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="delivery_pkey", referencedColumnName = "id")
    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private FoodCustomer foodcustomer;
    private String deliveryman_name;
    private String deliveryman_phone;
    private String deliveryman_email;
    private String deliveryman_address;
    private String Licence_no;
    private Boolean available;
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    @OneToOne(mappedBy = "deliverymanDetail",cascade = CascadeType.ALL)
    @JsonIgnore
    private DeliverymanRating deliverymanRating;
    @OneToOne
    @JoinColumn(name = "delivery_id", referencedColumnName = "delivery_id")
    @JsonIgnore
    private Delivery delivery;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonIgnore
    private OrderFood orderFood;

    @Embedded
    private LocationCal locationCal;

    @Enumerated(EnumType.STRING)
    private DeliverymanStatus status;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliverymanDetail deliverymanDetail = (DeliverymanDetail) o;
        return id != null && id.equals(deliverymanDetail.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public double getLatitude() {
        return locationCal.getLatitude();
    }

    public double getLongitude() {
        return locationCal.getLongitude();
    }
}
