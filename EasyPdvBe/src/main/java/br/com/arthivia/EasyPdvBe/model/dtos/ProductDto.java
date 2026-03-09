package br.com.arthivia.EasyPdvBe.model.dtos;

import java.math.BigDecimal;

public record ProductDto(
        Integer id,
        String name,
        String barcode,
        Integer category_id,
        BigDecimal sale_price,
        Boolean active
) {
    public ProductDto {
        if (id == null) {
            id = 0;
        }
        if (active == null) {
            active = true;
        }
    }
}
