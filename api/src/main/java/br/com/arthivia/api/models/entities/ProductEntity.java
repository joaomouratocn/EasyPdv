package br.com.arthivia.api.models.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private BigDecimal markup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measure_id")
    private MeasureEntity measure;
    private BigDecimal min_stock;
    private boolean alert_stock;
    @Column(updatable = false, insertable = false)
    private boolean active;
    @Column(updatable = false, insertable = false)
    private LocalDateTime created_at;
}
