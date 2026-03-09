package br.com.arthivia.EasyPdvBe.services;

import br.com.arthivia.EasyPdvBe.model.dtos.ProductDto;
import br.com.arthivia.EasyPdvBe.model.entities.ProductEntity;
import br.com.arthivia.EasyPdvBe.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public String insertProduct(ProductDto productDto) {
        var newProduct = new ProductEntity(productDto);
        productRepository.save(newProduct);
        return "Sucesso!";
    }

    public String updateProduct(int id, ProductDto productDto) {
        var product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        product.setName(productDto.name().toUpperCase());
        product.setBarcode(productDto.barcode().toUpperCase());
        product.setSalePrice(productDto.sale_price());
        product.setCategory_id(productDto.category_id());
        product.setActive(productDto.active());
        productRepository.save(product);
        return "Sucesso!";
    }

    public String deleteProduct(int id) {
        var product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        productRepository.delete(product);
        return "Sucesso!";
    }

    public List<ProductDto> getProductsByName(String name) {
        var products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream().map(ProductEntity::toProductDto).collect(Collectors.toList());
    }

    public ProductDto getProductById(int id) {
        var productFound = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return productFound.toProductDto();
    }

    public List<ProductDto> getAllProducts() {
        var products = productRepository.findAll();
        return products.stream().map(ProductEntity::toProductDto).collect(Collectors.toList());
    }
}
