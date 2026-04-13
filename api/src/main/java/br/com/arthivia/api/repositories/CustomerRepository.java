package br.com.arthivia.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.arthivia.api.models.entities.CustomerEntity;
import jakarta.transaction.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID>{
    List<CustomerEntity> findAllByActiveTrue();
    Optional<CustomerEntity> findByCpf(String cpf);
    @Modifying
    @Transactional
    @Query("UPDATE CustomerEntity c SET c.active = false WHERE c.id = :id")
    void disableCustomer(UUID id);
}
