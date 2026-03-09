package br.com.arthivia.EasyPdvBe.model.entities;

import br.com.arthivia.EasyPdvBe.model.dtos.ProductDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String barcode;
    private int Category_id;
    private BigDecimal salePrice;
    private boolean active;

    public ProductEntity(ProductDto productDto) {
        this.name = productDto.name().toUpperCase();
        this.barcode = productDto.barcode().toUpperCase();
        this.Category_id = productDto.category_id();
        this.salePrice = productDto.sale_price();
        this.active = productDto.active();
    }

    public ProductDto toProductDto() {
        return new ProductDto(
                this.getId(),
                this.getName(),
                this.getBarcode(),
                this.getCategory_id(),
                this.getSalePrice(),
                this.isActive()
        );
    }
}
