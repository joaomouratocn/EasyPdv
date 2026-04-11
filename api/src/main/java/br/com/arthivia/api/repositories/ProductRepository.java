package br.com.arthivia.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.arthivia.api.models.entities.ProductEntity;
import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findAllByActiveTrue();
    Optional<ProductEntity> findByIdAndActiveTrue(UUID id);
    Optional<ProductEntity> findByBarcodeAndActiveTrue(String barcode);
    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.active = false where p.id = :id")
    void disableProduct(UUID id);
}
