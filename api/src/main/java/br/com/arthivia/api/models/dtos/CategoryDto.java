package br.com.arthivia.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(
        String id,
        @NotNull
        @NotBlank
        String name
) {}
