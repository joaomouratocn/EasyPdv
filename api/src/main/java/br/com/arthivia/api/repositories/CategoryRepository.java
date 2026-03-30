package br.com.arthivia.api.repositories;

import br.com.arthivia.api.models.entities.CategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);

    List<CategoryEntity> findAllByActiveTrue();

    @Modifying
    @Transactional
    @Query("UPDATE CategoryEntity c SET c.active = false WHERE c.id = :id")
    void disableCategory(Long id);
}
