package br.com.arthivia.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MeasureDto(
        UUID id,
        @NotNull
        @NotBlank
        String name
) {}
