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
        CategoryDto category,
        @NotNull
        MeasureDto measure,
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
                new CategoryDto(productEntity.getCategory()),
                new MeasureDto(productEntity.getMeasure()),
                productEntity.getStock(),
                productEntity.getBuy_price(),
                productEntity.getSale_price(),
                productEntity.getMin_stock(),
                productEntity.isAlert_stock()
        );
    }
}
