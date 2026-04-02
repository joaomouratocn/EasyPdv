package br.com.arthivia.api.controllers;

import br.com.arthivia.api.models.dtos.ProductDto;
import br.com.arthivia.api.services.ProductService;
import br.com.arthivia.api.util.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse> createProduct(@RequestBody @Valid ProductDto productDto) {
        var result = productService.createProduct(productDto);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SuccessResponse> updateProduct(@PathVariable @Valid UUID id, @RequestBody @Valid ProductDto productDto) {
        var result = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponse> deleteProduct(@PathVariable @Valid UUID id) {
        var result = productService.deleteProduct(id);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> allProducts() {
        var result = productService.getAllProducts();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getProductsByName(@PathVariable @Valid UUID id) {
        var result = productService.getAllProductsById(id);
        return ResponseEntity.ok(result);
    }
}
