package br.com.arthivia.api.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.arthivia.api.models.dtos.ProductDto;
import br.com.arthivia.api.models.entities.CategoryEntity;
import br.com.arthivia.api.models.entities.MeasureEntity;
import br.com.arthivia.api.models.entities.ProductEntity;
import br.com.arthivia.api.repositories.ProductRepository;
import br.com.arthivia.api.util.Util;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public String createProduct(ProductDto productDto) {
        productRepository.findByBarcodeAndActiveTrue(productDto.barcode()).ifPresent(p -> {
            throw new RuntimeException("Product with the same barcode already exists");
        });

        var product = new ProductEntity();
        product.setName(Util.normalizeText(productDto.name()));
        product.setBarcode(productDto.barcode());
        product.setMarkup(productDto.markup());
        product.setCategory(new CategoryEntity(productDto.category()));
        product.setMeasure(new MeasureEntity(productDto.measure()));
        product.setDescription(productDto.description());
        product.setMin_stock(productDto.min_stock());
        product.setAlert_stock(productDto.alert_stock());
        productRepository.save(product);

        productRepository.save(product);

        return "Product created successfully.";
    }

    public String updateProduct(UUID id, ProductDto productDto) {
        var product = productRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Product with id '" + id + "' not found."));

        product.setName(Util.normalizeText(productDto.name()));
        product.setBarcode(productDto.barcode());
        product.setCategory(new CategoryEntity(productDto.category()));
        product.setMeasure(new MeasureEntity(productDto.measure()));
        product.setDescription(productDto.description());
        product.setMin_stock(productDto.min_stock());
        product.setAlert_stock(productDto.alert_stock());
        productRepository.save(product);

        return "Product updated successfully.";
    }

    public String deleteProduct(UUID id) {
        productRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Product with id '" + id + "' not found."));
        productRepository.disableProduct(id);
        return "Product deleted successfully.";
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAllByActiveTrue().stream().map(ProductDto::new).toList();
    }

    public ProductDto getAllProductsById(UUID id) {
        var productEntity = productRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Product with id '" + id + "' not found."));
        return new ProductDto(productEntity);
    }
}
