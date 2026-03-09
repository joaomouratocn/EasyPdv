package br.com.arthivia.EasyPdvBe.model.entities;

import br.com.arthivia.EasyPdvBe.model.dtos.CategoryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;

    public CategoryEntity(String name) {
        this.name = name.toUpperCase();
    }

    public CategoryDto toCategoryDto() {
        return new CategoryDto(id, name);
    }
}

