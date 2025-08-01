package com.cooola.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(name = "reserved_quantity", nullable = false, precision = 10, scale = 3)
    private BigDecimal reservedQuantity = BigDecimal.ZERO;

    @Column(name = "available_quantity", nullable = false, precision = 10, scale = 3)
    private BigDecimal availableQuantity = BigDecimal.ZERO;

    @Column(name = "unit_cost", precision = 10, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    @PrePersist
    public void calculateAvailableQuantity() {
        this.availableQuantity = this.quantity.subtract(this.reservedQuantity);
    }
}