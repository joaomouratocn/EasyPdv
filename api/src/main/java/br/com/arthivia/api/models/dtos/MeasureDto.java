package br.com.arthivia.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import br.com.arthivia.api.models.entities.MeasureEntity;

public record MeasureDto(
        UUID id,
        @NotNull
        @NotBlank
        String name
) {

    public MeasureDto(MeasureEntity measure) {
        this(
                measure.getId(),
                measure.getName()
        );
    }}
