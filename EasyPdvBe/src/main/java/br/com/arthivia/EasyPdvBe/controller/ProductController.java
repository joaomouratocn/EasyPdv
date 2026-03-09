package br.com.arthivia.EasyPdvBe.controller;

import br.com.arthivia.EasyPdvBe.model.dtos.ProductDto;
import br.com.arthivia.EasyPdvBe.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/insert")
    public String insertProduct(@RequestBody @Valid ProductDto productDto) {
        return productService.insertProduct(productDto);
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable int id, @RequestBody @Valid ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/search")
    public List<ProductDto> getProductsByName(@RequestParam String name) {
        return productService.getProductsByName(name);
    }

    @GetMapping("/get/{id}")
    public ProductDto getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/all")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
