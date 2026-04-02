package br.com.arthivia.api.models.dtos;

import br.com.arthivia.api.models.entities.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto(
        UUID id,
        @NotNull
        @NotBlank
        String name,
        String description,
        @NotNull
        @NotBlank
        String barcode,
        @NotNull
        UUID category_id,
        @NotNull
        UUID measure_id,
        @NotNull
        @Positive
        BigDecimal stock,
        @NotNull
        @Positive
        BigDecimal buy_price,
        @NotNull
        @Positive
        BigDecimal sale_price,
        @NotNull
        @Positive
        BigDecimal min_stock,
        @NotNull
        boolean alert_stock
) {
    public ProductDto(ProductEntity productEntity) {
        this(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getBarcode(),
                productEntity.getCategory_id(),
                productEntity.getMeasure_id(),
                productEntity.getStock(),
                productEntity.getBuy_price(),
                productEntity.getSale_price(),
                productEntity.getMin_stock(),
                productEntity.isAlert_stock()
        );
    }
}
