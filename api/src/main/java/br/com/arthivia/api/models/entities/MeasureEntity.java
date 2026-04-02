package br.com.arthivia.api.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import br.com.arthivia.api.models.dtos.MeasureDto;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "measure")
public class MeasureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private boolean active;

    public MeasureEntity(MeasureDto measureDto) {
        this.id = measureDto.id();
        this.name = measureDto.name();

    }
}
