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
    private UUID category_id;
    private UUID measure_id;
    private BigDecimal stock;
    private BigDecimal buy_price;
    private BigDecimal sale_price;
    private BigDecimal min_stock;
    private boolean alert_stock;
    private boolean active;
    private LocalDateTime created_at;
}
