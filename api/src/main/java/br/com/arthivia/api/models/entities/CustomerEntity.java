package br.com.arthivia.api.models.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.arthivia.api.models.dtos.CustomerDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customer")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String cpf;
    private String phone;
    @Column(insertable = false, updatable = false)
    private Boolean active;
    @Column(insertable = false, updatable = false)
    private BigDecimal limit;
    @Column(insertable = false, updatable = false)
    private LocalDateTime created_at;

    public CustomerEntity(CustomerDto customerDto) {
        this.id = customerDto.id();
        this.name = customerDto.name();
        this.cpf = customerDto.cpf();
        this.phone = customerDto.phone(); 
    }
}
