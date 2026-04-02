package br.com.arthivia.api.repositories;

import br.com.arthivia.api.models.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findAllByActiveTrue();
    Optional<ProductEntity> findByIdAndActiveTrue(UUID id);
    Optional<ProductEntity> findByBarcodeAndActiveTrue(String barcode);
}
