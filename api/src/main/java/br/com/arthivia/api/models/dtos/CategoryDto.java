package br.com.arthivia.api.models.dtos;

import java.util.UUID;

import br.com.arthivia.api.models.entities.CategoryEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(
        UUID id,
        @NotNull
        @NotBlank
        String name
) {

    public CategoryDto(CategoryEntity category) {
        this(
                category.getId(),
                category.getName()
        );
    }}
