package br.com.arthivia.api.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import br.com.arthivia.api.models.dtos.CategoryDto;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private boolean active;

    public CategoryEntity(CategoryDto category) {
        this.id = category.id();
        this.name = category.name();
    }
}