package br.com.arthivia.api.services;

import br.com.arthivia.api.models.dtos.ProductDto;
import br.com.arthivia.api.models.entities.ProductEntity;
import br.com.arthivia.api.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public String createProduct(ProductDto productDto) {
        productRepository.findByBarcodeAndActiveTrue(productDto.barcode()).ifPresent(p -> {
            throw new RuntimeException("Product with the same barcode already exists");
        });

        var product = new ProductEntity();
        product.setName(productDto.name());
        product.setBarcode(productDto.barcode());
        product.setCategory_id(productDto.category_id());
        product.setMeasure_id(productDto.measure_id());
        product.setDescription(productDto.description());
        product.setStock(productDto.stock());
        product.setBuy_price(productDto.buy_price());
        product.setSale_price(productDto.sale_price());
        product.setStock(productDto.stock());
        product.setMin_stock(productDto.min_stock());
        product.setAlert_stock(productDto.alert_stock());
        productRepository.save(product);

        productRepository.save(product);

        return "Product created successfully.";
    }

    public String updateProduct(UUID id, ProductDto productDto) {
        var product = productRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Product with id '" + id + "' not found."));

        product.setName(productDto.name());
        product.setBarcode(productDto.barcode());
        product.setCategory_id(productDto.category_id());
        product.setMeasure_id(productDto.measure_id());
        product.setDescription(productDto.description());
        product.setStock(productDto.stock());
        product.setBuy_price(productDto.buy_price());
        product.setSale_price(productDto.sale_price());
        product.setStock(productDto.stock());
        product.setMin_stock(productDto.min_stock());
        product.setAlert_stock(productDto.alert_stock());
        productRepository.save(product);

        return "Product updated successfully.";
    }

    public String deleteProduct(UUID id) {
        var product = productRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Product with id '" + id + "' not found."));
        product.setActive(false);
        productRepository.save(product);

        return "Product deleted successfully.";
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAllByActiveTrue().stream().map(ProductDto::new).toList();
    }

    public List<ProductDto> getAllProductsByName(String name) {
        return productRepository.findByNameIgnoreCaseAndActiveTrue(name).stream().map(ProductDto::new).toList();
    }
}
