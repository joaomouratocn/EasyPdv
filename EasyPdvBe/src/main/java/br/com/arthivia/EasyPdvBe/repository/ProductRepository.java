package br.com.arthivia.EasyPdvBe.repository;

import br.com.arthivia.EasyPdvBe.model.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    Optional<ProductEntity> findByNameContainingIgnoreCase(String name);
}
