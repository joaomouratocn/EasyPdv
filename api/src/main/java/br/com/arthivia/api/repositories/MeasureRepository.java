package br.com.arthivia.api.repositories;

import br.com.arthivia.api.models.entities.MeasureEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeasureRepository extends JpaRepository<MeasureEntity, UUID> {
    Optional<MeasureEntity> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE MeasureEntity m SET m.active = false WHERE m.id = :id")
    void disableMeasure(UUID id);

    List<MeasureEntity> findByActiveTrue();
}
