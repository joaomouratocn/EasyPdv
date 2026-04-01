package br.com.arthivia.api.models.dtos;

import java.util.UUID;

public record MeasureDto(
        UUID id,
        String name
) {}
