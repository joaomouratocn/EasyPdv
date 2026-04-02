package br.com.arthivia.api.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private String barcode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measure_id")
    private MeasureEntity measure;
    private BigDecimal stock;
    private BigDecimal buy_price;
    private BigDecimal sale_price;
    private BigDecimal min_stock;
    private boolean alert_stock;
    private boolean active;
    @Column(updatable = false)
    private LocalDateTime created_at;
}
